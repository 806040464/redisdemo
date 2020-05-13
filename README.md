# Redis分布式锁
分布式锁的本质目标就是在Redis占一个“坑”，当别的进程来占坑时，发现已经有一根“大萝卜”了，只好放弃或者稍后再试
占坑一般使用setnx（set if not exists）指令，只允许一个人占坑，先到先得，再用del指令释放“坑”


```
>setnx lock:key true
OK
... do something critical ...
>del lock:key
(integer) 1
```
有个问题，如果执行到中间环节出现异常，可能导致del指令没调用，就会陷入死锁，锁永远得不到释放
于是，拿到锁后加上失效时间，保证即时出现异常锁也会自动释放
```
>setnx lock:key true
OK
>expire lock:key 5
... do something critical ...
>del lock:key
(integer) 1
```
以上逻辑还有问题，在setnx与expire中间服务挂掉了，导致expire得不到执行，也会造成死锁
```
set lock:key true ex 5 nx
OK
... do something critical ...
>del lock:key
(integer) 1
```
上面指令就是setnx与expire组合的原子指令，就是分布式锁的奥义所在

### 可重入锁
可重入性是指线程在持有锁的情况下再次请求锁，如果一个锁支持同一线程多次加锁，就是可重入的，例如java的ReentrantLock。

Redis分布式锁如果要支持可重入，需要使用ThreadLocal变量存储当前持有锁的计数。

代码中实现**Jdeis**与**redistemplate**两种实现，有错误欢迎大家指出