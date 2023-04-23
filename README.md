# 微服务广告系统 /  ad-spring-cloud
## 广告系统的开发历程以及整体架构说明 ------*author：ZhanXiaowei*

***功能详细说明文档在pdf中可以查看***。

### 个人开发过程小结

整个广告系统的学习过程以及编码落地实现本人大约花费了十三、十四天的时间完成，因为中间还涉及到了系统开发的过程中去学习微服务的内容，还有准备面试等缘故，因此花费时间比较多，不过整体的收获颇丰，
学习到了许多业务思想还有框架技术方面的内容。在完成项目过程中，**本人有意识地去思考查阅软件工程方面的知识**，不管面试还是实际开发项目，本人越来越觉得相关方面需要有意识的去提升！**技术固然重要，
思维也要重视！**

1.业务方面具体到对整个系统的设计流程，从需求分析到数据模型构建（数据库设计），再到尝试绘制用例图，再到概要设计、详细设计，最后落地实现代码，还总体来说是花费了比较大的功夫，自己也主动尝试了
UML图、架构图等,最后采用黑盒测试的方法测试代码功能。
简单说说业务方面的一些思考，比如用户创建这个功能，站在用户角度去思考，需要输入哪些内容可以创建；比如广告主投放广告，站在广告主的角度思考，投递一个广告时，需要输入哪些类型的数据？广告内容？广告价格？广告区域？广告时间？....

2.技术方面具体到一些以前写一些简单项目的时候，没怎么用过但却非常实用的注解，具体内容的学习笔者记录在自己本地电脑的笔记中，就不细说了。

**广告系统的整个周期流程属于软件生命周期模型的瀑布模型**，瀑布模型虽然存在许多缺点，但是广告系统的架构设计比较明确，一般不会对系统进行大规模改动，而且此项目只是作为学习的用途，
追求高效快速开发，暂时没有进一步迭代更新系统的想法。

**个人收获**:
首先收获最大的是学习了MySQL Binlog的相关知识，对binlog理解更加深刻，了解了canal以及mysql-binlog-connector-java（项目中使用）两个开源工具；

学习了kafka的使用和基本原理；

对SpringCloud相关组件的使用更加熟练：eureka，zuul，hystrix；其实之前看的是alibaba那套，nacos、gateway等等，不过原理都差不多，以后应该会找相关的项目

### 广告系统架构
JDK1.8

MySQL 8+

Maven 3+

Spring cloud Hoxton.SR12

Eureka

Zuul / gateway

Feign

...

Spring boot 2.3.12.RELEASE

Kafka 2.13-3.4.0

MySQL Binlog
<br>

***项目结构:***

![image](https://user-images.githubusercontent.com/108646506/233827050-9106868c-b576-48f6-928a-e0bbcc4532b8.png)

***项目架构:***
![image](https://user-images.githubusercontent.com/108646506/233827173-c54e17e0-dc4c-4f2f-a254-1e892cf9c99c.png)

***工程结构：***

![image](https://user-images.githubusercontent.com/108646506/233827271-e6cafcc7-fbff-4d9a-b649-57df48b0cdc9.png)



##遇到的问题与解决方案

1.关于SpringCloud和springboot版本冲突问题，这个其实初学微服务相关知识的时候就已经了解过了，但是做项目的时候没注意，导致pom文件的依赖引入一直报错，去网上搜索一下对应版本修改即可

2.eureka集群搭建出现了问题，配置如下：
```
---
spring:
  application:
    name: ad-eureka
  profiles: server1
server:
  port: 8000
eureka:
  instance:
    hostname: server1
    prefer-ip-address: false
  client:
    fetchRegistry: true
    registerWithEureka: true
    service-url:
      defaultZone: http://server2:8001/eureka/,http://server3:8002/eureka/

---
spring:
  application:
    name: ad-eureka
  profiles: server2
server:
  port: 8001
eureka:
  instance:
    hostname: server2
    prefer-ip-address: false
  client:
    fetchRegistry: true
    registerWithEureka: true
    service-url:
      defaultZone: http://server1:8000/eureka/,http://server3:8002/eureka/

---
spring:
  application:
    name: ad-eureka
  profiles: server3
server:
  port: 8002
eureka:
  instance:
    hostname: server3
    prefer-ip-address: false
  client:
    fetchRegistry: true
    registerWithEureka: true
    service-url:
      defaultZone: http://server1:8000/eureka/,http://server2:8001/eureka/
```
按照上述配置，发现集群无法互相注册，
主要原因是在Windows中一定要注意修改C:\Windows\System32\drivers\etc 下的hosts文件，原因后面解释，本人就是因为没有修改hosts文件而一直出现问题。

##### 查阅了许多资料以后，我总结出了配置集群的注意点：

1.各个节点的spring.application.name要保持一致，比如我的三个节点都是ad-eureka

2.本地测试时各个节点都在同一台机器，hostname需要在本地host中填写，各个节点使用自己的host，比如我的三个节点hostname分别是server1,server2,server3;

3.eureka.client.serviceUrl.defaultZone配置项的地址，不能使用localhost，要使用域名，**修改hosts文件**，一定注意不可以使用localhost作为hostname！！

```也就是defaultZone那里不能写为：http://localhost:8000/eureka/ ```

hosts文件就在后面添加下面三行:
```
127.0.0.1 server1
127.0.0.1 server2
127.0.0.1 server3
```
4.eureka.instance.prefer-ip-address参数要设置为false,为true时会以ip形式注册，导致最后解析出来的 hostname 都是相同的IP，使副本不可用。

5.如下两个参数需配为true
```
> eureka.client.register-with-eureka=true   \\ false表示自己就是注册中心，不用注册自己

> eureka.client.fetch-registry=true				  \\ false表示自己就是注册中心，不用去注册中心获取其他服务的地址
```


## 编写单元测试用例，修正代码错误

本来这部分打算写进功能说明的文档，但是有点强迫症，功能说明不想加入其它的内容，就写在这里吧。

### 单元测试说明(测试覆盖率50%)

由于本人水平有限，主要采取黑盒测试（白盒测试有点多而且难），接口测试覆盖率至少50%，实际测试的时候许多测试用例忘记保存了，这里就只剩下主要的测试用例了.......

**一、广告用户服务单元测试用例编写：**
```
@RunWith(SpringRunner.class)
@SpringBootTest(
        classes = {SponsorApplication.class},
        webEnvironment = SpringBootTest.WebEnvironment.NONE
)
public class UserServiceTest {
    @Autowired
    private IUserService userService;
    @Test
    // @Transactional
    public void testCreateUser() throws AdException {
//        测试创建用户
        CreateUserRequest createUserRequest=new CreateUserRequest("ZhanXiaowei");
        System.out.println(userService.createUser(createUserRequest));
    }
}
```

通过测试，新增了一条用户记录

![image](https://user-images.githubusercontent.com/108646506/233829267-c5cb3f1e-7e18-4e36-af10-43038da0ce63.png)

**二、广告 投递单元 测试用例编写：ad-sponsor**
测试用例编写在模块ad-sponsor\src\test\java\com\ad\service目录下

**Ad_PlanServiceTest**

主要是增删改查的测试。

```
//      测试查询,顺利通过测试，
        System.out.println(planService.getAdPlanByIds(new AdPlanGetRequest(15L, Collections.singletonList(10L)))
        );
```

![image](https://user-images.githubusercontent.com/108646506/233829492-517c0be7-aa71-4bc7-aa3a-42b2f4d3b64d.png)

```
//      测试新增
        System.out.println(planService.createAdPlan(new AdPlanRequest(8L, 21L, "测试新增计划1", "2023.04.06", "2023.04.25"))
        );
        System.out.println(planService.createAdPlan(new AdPlanRequest(9L, 22L, "测试新增计划2", "2023.04.06", "2023.04.25"))
        );
        System.out.println(planService.createAdPlan(new AdPlanRequest(11L, 23L, "测试新增计划3", "2023.04.06", "2023.04.25"))
        );
//      测试更新
        System.out.println(planService.updateAdPlan(new AdPlanRequest(10L, 15L, "测试更新计划", "2022.08.07", "2023.04.07"))
        );
```

![image](https://user-images.githubusercontent.com/108646506/233829556-9ebc6316-846e-44e2-930f-e4c956c9c82d.png)

测试没通过，经过debug以及代码复查发现居然是下面的代码错误了。。。真的是，笔者粗心的几行代码，debug了一个小时。。。

![image](https://user-images.githubusercontent.com/108646506/233829620-ed0e1e0c-efb5-468d-8e34-a32dfa9dbfc2.png)

修改后顺利通过，ad_plan表中新增了三个字段，更新功能也通过了。

![image](https://user-images.githubusercontent.com/108646506/233829636-626f7e17-ed7a-46a9-a9ff-57d18f081ccd.png)


```
//      测试删除
        planService.deleteAdPlan(new AdPlanRequest(9L, 22L, "测试删除计划2", "2023.04.06", "2023.04.25")
        );
```

delete方法居然也失败了，发现也是忘记取反了。。。

![image](https://user-images.githubusercontent.com/108646506/233829649-d7d5ee29-7ee6-4496-aa10-2e08fd93fdba.png)

修改后成功删除用户id为22、广告id为12的广告计划。

***其他的测试用例对应的功能的结果：***

就不一一列举了，部分功能简单测试看看结果

1.再次执行同样的新增方法的代码，比如执行相同的新增广告计划方法，会返还告诉我们重复创建的异常

![image](https://user-images.githubusercontent.com/108646506/233829697-9064517a-c928-48e5-8d36-2da8c523d6c4.png)

2.删除不存在的广告计划

![image](https://user-images.githubusercontent.com/108646506/233829706-78b78791-1d9e-4681-90a8-8a697a8054a5.png)

3.新增广告单元、新增“广告单元-创意”关联------**AdUnitServiceTest**(看注释)
```
//      测试新建广告推广单元  新增的数据在表ad_unit中
        System.out.println(adUnitService.createUnit(new AdUnitRequest(11L, "推广单元测试", 2, 200000L)));
//      测试新建  广告推广单元和创意 的关联表；即每个推广单元和创意是多对多的关系  新增的数据在表creative_unit中
//      测试过程，先new获取新对象，因为传入的参数的类中含有静态内部类，因此要记得初始化！否则会报空指针异常！
        CreativeUnitRequest creativeUnitRequest = new CreativeUnitRequest();
        List<CreativeUnitRequest.CreativeUnitItem> unitItems = creativeUnitRequest.getUnitItems();
        unitItems=new ArrayList<>();//要初始化，不然报空指针异常
//      设置好 “广告单元-创意”的id关联关系
        CreativeUnitRequest.CreativeUnitItem creativeUnitItem = new CreativeUnitRequest.CreativeUnitItem();
        creativeUnitItem.setCreativeId(10L);
        creativeUnitItem.setUnitId(12L);
//      加入要传入的参数
        unitItems.add(creativeUnitItem);
        System.out.println(adUnitService.createCreativeUnit(new CreativeUnitRequest(unitItems))
```

**三、广告 检索单元 测试用例编写 ad-search**

详细的测试代码太长了，就不展示了，代码中已经有详细的注释了

**四、测试controller接口：**

这里我就直接放出测试截图了

```测试新建一个数据库中已经存在的用户```

![image](https://user-images.githubusercontent.com/108646506/233829867-624deea7-b50e-4965-ab69-728950e77f8c.png)

结果如下：

![image](https://user-images.githubusercontent.com/108646506/233829883-7ec0bd1d-c532-402d-a722-fa7c3025bbba.png)

这里需要说明的是我们定义的ad-common微服务模块中，包含了统一响应和统一异常的实现，所以才会返回上图的内容。

```测试网关```

我们网关的接口定义为9090，网关前缀是/gateway

也就是说测试的时候，输入的url为 127.0.0.1:9090/gateway/ad-sponsor/create/user

结果：控制台也会打印网关的相关信息（包括我们自己实现的过滤器信息等等）。笔者在网关的实现这部分上并没有进行特别的操作，主要是系统没有实现登录注册等等功能，因此都是可以直接绕过网关进行操作的，而网关这部分笔者主要是学习如何使用，因此实现的时候只实现了统计一个请求前后的时间差，就贴个后置过滤器的代码吧，前置的就不贴了，自己看。

```
//后置过滤器
public class AccessLogFilter extends ZuulFilter {
    @Override
    public String filterType() {
        return FilterConstants.POST_TYPE;
    }

    @Override
    public int filterOrder() {
        return FilterConstants.SEND_RESPONSE_FILTER_ORDER-1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
//        先获取上下文
        RequestContext currentContext = RequestContext.getCurrentContext();
//      从pre过滤器得到startTime
        Long startTime= (Long) currentContext.get("startTime");
// 从request获取URI
        HttpServletRequest request = currentContext.getRequest();
        String uri = request.getRequestURI();
//相减得到URI经过网关的时间
        long duration= System.currentTimeMillis()-startTime;
        log.info("uri:"+uri+",duration:"+duration/100+"ms");
        return null;
    }
}
```




