# android-ad-san

## 一、配置SDK Key
```
<manifest>
   <application>
        <meta-data android:name="com.san.APP_KEY"
            android:value="YOUR_APP_KEY"/>
   </application>
</manifest>
```

## 二、添加网络白名单
```
<manifest>
        <application
            ...
            android:networkSecurityConfig="@xml/network_security_config"
            ...>
        </application>
    </manifest>
```
```
<?xml version="1.0" encoding="utf-8"?>
    <network-security-config>
        ...
        <base-config cleartextTrafficPermitted="true">
            <trust-anchors>
                <certificates src="system"/>
            </trust-anchors>
        </base-config>
        ...
</network-security-config>
```

## 三、基本使用

### 1.集成时的依赖冲突，主要是（WorkManager和Exoplayer)
```
    implementation/api('com.atlasv.android:san:x.x.x-gz'){
        exclude group: 'androidx.work'
        exclude group: 'com.google.android.exoplayer'
        exclude group: 'com.google.guava'
    }
```

### 2.初始化
自定义好自己的广告id list，并在MainActivity的onCreate处初始化广告，为了防止activity被回收后重新创建导致build的广告对象不可用，必须在build之前clear掉之前的单例对象：
```kotlin
private val list = arrayListOf(AdType.NATIVE to id,...)
AdManager.instance.clear()
list.forEach {
    AdManager.instance.buildAd(context, it.first, it.second)
}
```

### 3、插屏广告
插屏广告需要提前预加载，插屏和原生不同，原生不做预加载，在使用场景中load成功后，也会去show。插屏为了保证使用场景的一致性，未预加载好则不显示
```kotlin
prepare：AdManager.instance.getAd(id)?.prepare()
show：
val ad = AdManager.instance.getAd(id)
if (ad?.isReady() == true) {
    ad.show()
}
```

### 4、接入埋点统计
```kotlin
AdManager.instance.setAnalyticsListener(object : AnalyticsListener {
    override fun logEvent(event: String, bundle: Bundle?) {
        FirebaseAnalytics.getInstance(applicationContext).logEvent(event, bundle)
    }
})
```

## 三、文档说明
https://doc.etm.tech/wiki#/wiki/ads/0x00-ads-life-circle

https://github.com/san-sdk/sample/wiki/Integrate-the-SAN-SDK
