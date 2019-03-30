# IOC,DI
## xml配置
### beans根标签
### bean
1. name
2. class
3. factory-method
4. factory-bean
5. init-method
6. destroy-method
7. scope
    - singleton 
    - prototype 
    - request 
    - session 
    - globalSession 
    
    
### 属性注入
### property set注入
1. name
2. value
3. ref

### constructor-arg 构造方法注入
1. name
2. value
3. ref

### p命名空间注入
xmlns:p="http://www.springframework.org/schema/p"

```xml
<bean name="person" class="classname" p:name="uu" p:car2-ref="car2"/>
```
### SpEl注入

```xml
<bean id="car" class="classname">
    <property name="name" value="#{'奔驰'}"/>
    <property name="price" value="#{800000}"/>
</bean>
```

### 复杂类型
复杂类型放入property中：  
1. 数组，list
```xml
<list>
    <value>1</value>
    <value>2</value>
    <value>3</value>
</list>
```
2. map
```xml
<map>
    <entry key="aaa" value="111"/>
    <entry key="bbb" value="222"/>
    <entry key="ccc" value="333"/>
</map>
```


2. property 
```xml
<props>
    <prop key="username">root</prop>
    <prop key="password">123</prop>
</props>
```
### 多配置

1. 加载多个
```java
ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext.xml","applicationContext2.xml");
```

2. 引入另一个
```xml
<import resource="applicationContext2.xml"></import>
```
## 注解
* @Component
* @Controller :WEB 层
* @Service :业务层
* @Repository :持久层


1. @Value :用于注入普通类型.
2. @Autowired :自动装配:
3. @Qualifier:强制使用名称注入.
4. @Resource 相当于:@Autowired 和@Qualifier 一起使用
5. @Scope
6. @PostConstruct
7. @PreDestroy


# AOP

代理机制:
- Spring 的 AOP 的底层用到两种代理机制：
     * JDK 的动态代理 :针对实现了接口的类产生代理.
     * Cglib 的动态代理 :针对没有实现接口的类产生代理. 应用的是底层的字节码增强的技术 生成当前类的子类对象.

###  JDK 动态代理
```java
public class MyJDKProxy implements InvocationHandler {
    private UserDao userDao;
    public MyJDKProxy(UserDao userDao) {
        this.userDao = userDao;
    }
    // 编写工具方法：生成代理：
    public UserDao createProxy(){
        UserDao userDaoProxy = (UserDao) 
        Proxy.newProxyInstance(userDao.getClass().getClassLoader(),
        userDao.getClass().getInterfaces(), this);
        return userDaoProxy;
    }
    
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if("save".equals(method.getName())){
            System.out.println("权限校验================");
        }
        return method.invoke(userDao, args);
    }
}
```

###  Cglib 动态代理
```java
public class MyCglibProxy implements MethodInterceptor{
    private CustomerDao customerDao;
    public MyCglibProxy(CustomerDao customerDao){
        this.customerDao = customerDao;
    }
    // 生成代理的方法:
    public CustomerDao createProxy(){
        // 创建 Cglib 的核心类:
        Enhancer enhancer = new Enhancer();
        // 设置父类:
        enhancer.setSuperclass(CustomerDao.class);
        // 设置回调:
        enhancer.setCallback(this);
        // 生成代理：
        CustomerDao customerDaoProxy = (CustomerDao) enhancer.create();
        return customerDaoProxy;
    }
    @Override
    public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        if("delete".equals(method.getName())){
            Object obj = methodProxy.invokeSuper(proxy, args);
            System.out.println("日志记录================");
            return obj;
        }
        return methodProxy.invokeSuper(proxy, args);
    }
}
```

1. Joinpoint(连接点):
2. Pointcut(切入点):
3. Advice(通知/增强):
4. Target(目标对象):
5. Weaving(织入):
6. Proxy（代理）:
8. Aspect(切面):  

AOP约束
```xml
<beans xmlns="http://www.springframework.org/schema/beans"
 xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
 xmlns:aop="http://www.springframework.org/schema/aop" xsi:schemaLocation="
 http://www.springframework.org/schema/beans 
http://www.springframework.org/schema/beans/spring-beans.xsd
 http://www.springframework.org/schema/aop 
http://www.springframework.org/schema/aop/spring-aop.xsd">
</beans>
```

```xml
<!-- 配置切面类 -->
<bean id="myAspectXml" class="cn.itcast.spring.demo3.MyAspectXml"></bean>
<!-- 进行 aop 的配置 -->
<aop:config>
    <!-- pointcut切入点   expression切入点表达式、 execution(* 之后一定要有空格 -->
    <aop:pointcut expression="execution(* cn.itcast.spring.demo3.*Dao.save(..))" id="pointcut1"/>
    <aop:pointcut expression="execution(* cn.itcast.spring.demo3.*Dao.delete(..))" id="pointcut2"/>
    <aop:pointcut expression="execution(* cn.itcast.spring.demo3.*Dao.update(..))" id="pointcut3"/>
    <aop:pointcut expression="execution(* cn.itcast.spring.demo3.*Dao.find(..))" id="pointcut4"/>
    <!-- 配置切面 -->
    <aop:aspect ref="myAspectXml">
        <aop:before method="before" pointcut-ref="pointcut1"/>
        <aop:after-returning method="afterReturing" pointcut-ref="pointcut2"/>
        <aop:around method="around" pointcut-ref="pointcut3"/>
        <aop:after-throwing method="afterThrowing" pointcut-ref="pointcut4"/>
        <aop:after method="after" pointcut-ref="pointcut4"/>
    </aop:aspect>
</aop:config>
```

**报错不用怕：**  
```xml
<!-- https://mvnrepository.com/artifact/org.springframework/spring-aop -->
<dependency>
  <groupId>org.aspectj</groupId >
  <artifactId>aspectjweaver</artifactId >
  <version>1.6.11</version >
</dependency>

<!-- https://mvnrepository.com/artifact/cglib/cglib -->
<dependency>
  <groupId>cglib</groupId>
  <artifactId>cglib</artifactId>
  <version>2.2.2</version>
</dependency>
```
### aop注解
```java
@Aspect
public class MyAspectAnno {
    
    @Before("MyAspectAnno.pointcut1()")
    public void before(){
        System.out.println("前置通知===========");
    }
    
    @AfterReturning("MyAspectAnno.pointcut2()")
    public void afterReturning(){
        System.out.println("后置通知===========");
    }
    
    @Around("MyAspectAnno.pointcut3()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable{
        System.out.println("环绕前通知==========");
        Object obj = joinPoint.proceed();
        System.out.println("环绕后通知==========");
        return obj;
    }
    
    @AfterThrowing("MyAspectAnno.pointcut4()")
    public void afterThrowing(){
        System.out.println("异常抛出通知========");
    }
    
    @After("MyAspectAnno.pointcut4()")
    public void after(){
        System.out.println("最终通知==========");
    }
    
    @Pointcut("execution(* cn.itcast.spring.demo4.ProductDao.save(..))")
    private void pointcut1(){}
    @Pointcut("execution(* cn.itcast.spring.demo4.ProductDao.update(..))")
    private void pointcut2(){}
    @Pointcut("execution(* cn.itcast.spring.demo4.ProductDao.delete(..))")
    private void pointcut3(){}
    @Pointcut("execution(* cn.itcast.spring.demo4.ProductDao.find(..))")
    private void pointcut4(){}
}
```



# spring JDBC
### bean配置连接
```xml
<bean id="dataSource" class="com.mchange.v2.c3p0.ComboPooledDataSource">
 <property name="driverClass" value="com.mysql.jdbc.Driver"/>
 <property name="jdbcUrl" value="jdbc:mysql:///spring_day02"/>
 <property name="user" value="root"/>
 <property name="password" value="123"/>
</bean>
```
### 外部属性配置
```properties
jdbc.driverClass=com.mysql.jdbc.Driver
jdbc.url=jdbc:mysql:///spring_day02
jdbc.username=root
jdbc.password=123
```
### 外部属性文件引入
```xml
<bean class="org.springframework.beans.factory.config.PropertyPlaceholderConfigurer">
 <property name="location" value="classpath:jdbc.properties"/>
</bean>


<context:property-placeholder location="classpath:jdbc.properties"/>


<bean id="dataSource" class="com.mchange.v2.c3p0.ComboPooledDataSource">
 <property name="driverClass" value="${jdbc.driverClass}"/>
 <property name="jdbcUrl" value="${jdbc.url}"/>
 <property name="user" value="${jdbc.username}"/>
 <property name="password" value="${jdbc.password}"/>
</bean>
```

### 配置事务管理器
```xml
<bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
    <property name="dataSource" ref="dataSource"/>
</bean>
```

### 配置事务的通知
```xml
<!-- 配置事务的增强 -->
 <tx:advice id="txAdvice" transaction-manager="transactionManager">
     <tx:attributes>
         <!--
            isolation="DEFAULT" 隔离级别
            propagation="REQUIRED" 传播行为
            read-only="false" 只读
            timeout="-1" 过期时间
            rollback-for="" -Exception
            no-rollback-for="" +Exception
        -->
        <tx:method name="transfer" propagation="REQUIRED"/>
     </tx:attributes>
</tx:advice>
```

### 配置aop

```xml
<aop:config>
 <aop:pointcut expression="execution(* cn.itcast.transaction.demo2.AccountServiceImpl.transfer(..))" id="pointcut1"/>
 <aop:advisor advice-ref="txAdvice" pointcut-ref="pointcut1"/>
</aop:config>
```



### 注解的方式开发
```xml
<!-- 开启注解事务管理 -->
<tx:annotation-driven transaction-manager="transactionManager"/>
```

@Transactional 注解配置

### 事务传播行为
PROPAGION_XXX :事务的传播行为
 * 保证同一个事务中
    - PROPAGATION_REQUIRED 支持当前事务，如果不存在 就新建一个(默认)
    - PROPAGATION_SUPPORTS 支持当前事务，如果不存在，就不使用事务
    - PROPAGATION_MANDATORY 支持当前事务，如果不存在，抛出异常
* 保证没有在同一个事务中
    - PROPAGATION_REQUIRES_NEW 如果有事务存在，挂起当前事务，创建一个新的事务
    - PROPAGATION_NOT_SUPPORTED 以非事务方式运行，如果有事务存在，挂起当前事务
    - PROPAGATION_NEVER 以非事务方式运行，如果有事务存在，抛出异常
    - PROPAGATION_NESTED 如果当前事务存在，则嵌套事务执行





















