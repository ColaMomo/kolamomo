# JVM性能调优&故障处理工具


## jps

列出正在运行的jvm进程  
-l：输出主类的全名  
-v：输出虚拟机进程启动jvm的参数  

>jps -lv

```
6493 org.apache.catalina.startup.Bootstrap -Djava.util.logging.config.file=/data1/weibo-auth-manage/conf/logging.properties -Xms1g -Xmx1g -Xmn512m -DServer=mblog -XX:PermSize=128m -XX:MaxPermSize=128m -XX:MaxTenuringThreshold=4 -XX:+UseConcMarkSweepGC -XX:SurvivorRatio=8 -XX:CMSInitiatingOccupancyFraction=70 -XX:+ExplicitGCInvokesConcurrent -XX:+PrintFlagsFinal -XX:+PrintCommandLineFlags -XX:+PrintGCDateStamps -XX:+PrintTenuringDistribution -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -XX:+PrintGCApplicationStoppedTime -XX:+PrintGCApplicationConcurrentTime -Xloggc:../gclogs/gc.log.20160127_104912 -Djava.util.logging.manager=org.apache.juli.ClassLoaderLogManager -DWEIBO_CONF_PATH=/data1/weibo-auth-manage/weibo_conf -agentlib:jdwp=transport=dt_socket,address=8788,server=y,suspend=n -Djava.endorsed.dirs=/data1/weibo-auth-manage/endorsed -Dcatalina.base=/data1/weibo-auth-manage -Dcatalina.home=/data1/weibo-auth-manage -Djava.io.tmpdir=/tmp

...
```

## jstat

监视虚拟机各种运行状态信息

>jstat -class 6493 250 4  

监视类装载卸载情况，每250ms查询一次，共查询4次

```
Loaded  Bytes  Unloaded  Bytes     Time
  5812 11719.5        0     0.0       6.58
  5812 11719.5        0     0.0       6.58
  5812 11719.5        0     0.0       6.58
  5812 11719.5        0     0.0       6.58
```

>jstat -gc 6493

监视java堆的状况

```
 S0C    S1C    S0U    S1U      EC       EU        OC         OU       PC     PU    YGC     YGCT    FGC    FGCT     GCT
52416.0 52416.0  0.0   9747.6 419456.0 147334.4  524288.0   43507.4   131072.0 36822.2     39    1.293  62      1.006    2.299
```

>jstat -gccapacity 6493

类似-gc，主要关注java对各个区域使用到的最大最小空间

```
 NGCMN    NGCMX     NGC     S0C   S1C       EC      OGCMN      OGCMX       OGC         OC      PGCMN    PGCMX     PGC       PC     YGC    FGC
524288.0 524288.0 524288.0 52416.0 52416.0 419456.0   524288.0   524288.0   524288.0   524288.0 131072.0 131072.0 131072.0 131072.0     39    62
```

>jstat -gcutil 6493

类似-gc, 主要关注已使用空间占总空间的百分比

```
  S0     S1     E      O      P     YGC     YGCT    FGC    FGCT     GCT
  0.00  18.60  43.87   8.30  28.09     39    1.293    62    1.006    2.299
```

```
S0C：年轻代中第一个survivor（幸存区）的容量 (字节) 
S1C：年轻代中第二个survivor（幸存区）的容量 (字节) 
S0U：年轻代中第一个survivor（幸存区）目前已使用空间 (字节) 
S1U：年轻代中第二个survivor（幸存区）目前已使用空间 (字节) 
EC：年轻代中Eden（伊甸园）的容量 (字节) 
EU：年轻代中Eden（伊甸园）目前已使用空间 (字节) 
OC：Old代的容量 (字节) 
OU：Old代目前已使用空间 (字节) 
PC：Perm(持久代)的容量 (字节) 
PU：Perm(持久代)目前已使用空间 (字节) 
YGC：从应用程序启动到采样时年轻代中gc次数 
YGCT：从应用程序启动到采样时年轻代中gc所用时间(s) 
FGC：从应用程序启动到采样时old代(全gc)gc次数 
FGCT：从应用程序启动到采样时old代(全gc)gc所用时间(s) 
GCT：从应用程序启动到采样时gc用的总时间(s) 
NGCMN：年轻代(young)中初始化(最小)的大小 (字节) 
NGCMX：年轻代(young)的最大容量 (字节) 
NGC：年轻代(young)中当前的容量 (字节) 
OGCMN：old代中初始化(最小)的大小 (字节) 
OGCMX：old代的最大容量 (字节) 
OGC：old代当前新生成的容量 (字节) 
PGCMN：perm代中初始化(最小)的大小 (字节) 
PGCMX：perm代的最大容量 (字节)   
PGC：perm代当前新生成的容量 (字节) 
S0：年轻代中第一个survivor（幸存区）已使用的占当前容量百分比 
S1：年轻代中第二个survivor（幸存区）已使用的占当前容量百分比 
E：年轻代中Eden（伊甸园）已使用的占当前容量百分比 
O：old代已使用的占当前容量百分比 
P：perm代已使用的占当前容量百分比 
S0CMX：年轻代中第一个survivor（幸存区）的最大容量 (字节) 
S1CMX ：年轻代中第二个survivor（幸存区）的最大容量 (字节) 
ECMX：年轻代中Eden（伊甸园）的最大容量 (字节) 
DSS：当前需要survivor（幸存区）的容量 (字节)（Eden区已满） 
TT： 持有次数限制 
MTT ： 最大持有次数限制 
```

## jinfo 

实时查看和调整虚拟机的各项参数 

```
jinfo 6493
Attaching to process ID 6493, please wait...
Debugger attached successfully.
Server compiler detected.
JVM version is 24.75-b04
Java System Properties:

java.runtime.name = Java(TM) SE Runtime Environment
java.vm.version = 24.75-b04
sun.boot.library.path = /usr/jdk1.7.0_75/jre/lib/amd64
...

VM Flags:

-Djava.util.logging.config.file=/data1/weibo-auth-manage/conf/logging.properties -Xms1g -Xmx1g -Xmn512m -DServer=mblog -XX:PermSize=128m -XX:MaxPermSize=128m -XX:MaxTenuringThreshold=4 -XX:+UseConcMarkSweepGC -XX:SurvivorRatio=8 -XX:CMSInitiatingOccupancyFraction=70 -XX:+ExplicitGCInvokesConcurrent -XX:+PrintFlagsFinal -XX:+PrintCommandLineFlags -XX:+PrintGCDateStamps -XX:+PrintTenuringDistribution -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -XX:+PrintGCApplicationStoppedTime -XX:+PrintGCApplicationConcurrentTime -Xloggc:../gclogs/gc.log.20160127_104912 -Djava.util.logging.manager=org.apache.juli.ClassLoaderLogManager -DWEIBO_CONF_PATH=/data1/weibo-auth-manage/weibo_conf -agentlib:jdwp=transport=dt_socket,address=8788,server=y,suspend=n -Djava.endorsed.dirs=/data1/weibo-auth-manage/endorsed -Dcatalina.base=/data1/weibo-auth-manage -Dcatalina.home=/data1/weibo-auth-manage -Djava.io.tmpdir=/tmp
```

## jmap

>jmap -dump:format=b,file=weibo-auth-manage.bin 6493

生成java内存转储快照

```
Dumping heap to /data1/weibo-auth/bin/weibo-auth-manage.bin ...
Heap dump file created
```

>jmap -histo 6493

显示堆内存中对象的统计信息，包括类，实例数，占用总容量

```
num     #instances         #bytes  class name
----------------------------------------------
   1:        984011       94209312  [C
   2:        462476       59081112  [B
   3:        930262       29768384  java.util.concurrent.locks.AbstractQueuedSynchronizer$Node
   4:        595278       14286672  java.lang.String
   5:         72798       11164432  <constMethodKlass>
   6:         29776        9666736  [I
   7:         72798        9330160  <methodKlass>
   8:          6245        7667488  <constantPoolKlass>
   9:        115584        6936136  [Ljava.lang.Object;
  10:          6245        4519880  <instanceKlassKlass>
  11:          4980        4412096  <constantPoolCacheKlass>
  12:        100050        4002000  java.util.concurrent.ConcurrentHashMap$KeyIterator
  13:         58428        3739392  com.mysql.jdbc.ConnectionPropertiesImpl$BooleanConnectionProperty
  ...
```
> jmap -heap 6493

```
Attaching to process ID 6493, please wait...
Debugger attached successfully.
Server compiler detected.
JVM version is 24.75-b04

using parallel threads in the new generation.
using thread-local object allocation.
Concurrent Mark-Sweep GC

Heap Configuration:								//堆内存配置
   MinHeapFreeRatio = 40						//
   MaxHeapFreeRatio = 70
   MaxHeapSize      = 1073741824 (1024.0MB)	//最大堆内存（不包含永久代）
   NewSize          = 536870912 (512.0MB)		//YG初始化大小
   MaxNewSize       = 536870912 (512.0MB)		//YG最大大小
   OldSize          = 5439488 (5.1875MB)		//OG初始化大小
   NewRatio         = 2							//YG，OG比例为1：2
   SurvivorRatio    = 8							//YG中eden与survivor的比例8：1
   PermSize         = 134217728 (128.0MB)		//永久代大小
   MaxPermSize      = 134217728 (128.0MB)		//永久代最大值
   G1HeapRegionSize = 0 (0.0MB)

Heap Usage:
New Generation (Eden + 1 Survivor Space):
   capacity = 483196928 (460.8125MB)
   used     = 168274336 (160.47891235351562MB)
   free     = 314922592 (300.3335876464844MB)
   34.8252081602638% used
Eden Space:
   capacity = 429522944 (409.625MB)
   used     = 158401880 (151.06380462646484MB)
   free     = 271121064 (258.56119537353516MB)
   36.87856078766307% used
From Space:
   capacity = 53673984 (51.1875MB)
   used     = 9872456 (9.415107727050781MB)
   free     = 43801528 (41.77239227294922MB)
   18.393372848939254% used
To Space:
   capacity = 53673984 (51.1875MB)
   used     = 0 (0.0MB)
   free     = 53673984 (51.1875MB)
   0.0% used
concurrent mark-sweep generation:
   capacity = 536870912 (512.0MB)
   used     = 44544712 (42.48114776611328MB)
   free     = 492326200 (469.5188522338867MB)
   8.297099173069% used
Perm Generation:
   capacity = 134217728 (128.0MB)
   used     = 37682664 (35.936988830566406MB)
   free     = 96535064 (92.0630111694336MB)
   28.075772523880005% used

20995 interned Strings occupying 1964928 bytes.
```

## jstack

生成虚拟机当前时刻的线程快照  
-l: 除堆栈外，显示关于锁的附加信息  

>jstack -l 6493

```
2016-01-28 18:53:11
Full thread dump Java HotSpot(TM) 64-Bit Server VM (24.75-b04 mixed mode):

"Attach Listener" daemon prio=10 tid=0x00002b736c041800 nid=0x6f06 waiting on condition [0x0000000000000000]
   java.lang.Thread.State: RUNNABLE

   Locked ownable synchronizers:
        - None

"catalina-exec-101" daemon prio=10 tid=0x00002b7404003800 nid=0xd4c waiting on condition [0x00002b735069b000]
   java.lang.Thread.State: WAITING (parking)
        at sun.misc.Unsafe.park(Native Method)
        - parking to wait for  <0x00000000d9f983f8> (a java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject)
        at java.util.concurrent.locks.LockSupport.park(LockSupport.java:186)
        at java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject.await(AbstractQueuedSynchronizer.java:2043)
        at java.util.concurrent.LinkedBlockingQueue.take(LinkedBlockingQueue.java:442)
        at java.util.concurrent.ThreadPoolExecutor.getTask(ThreadPoolExecutor.java:1068)
        at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1130)
        at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:615)
        at java.lang.Thread.run(Thread.java:745)

   Locked ownable synchronizers:
        - None

...
```

