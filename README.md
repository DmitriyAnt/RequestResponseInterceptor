## Request Response Interceptor
A simple version of the request and response interceptor with full text search.
To integrate into your application, you need to add the RequestResponseInterceptor class to the builder OkHttpClient.Builder

``` kotlin
@Inject  
lateinit var interceptor: Lazy<RequestResponseInterceptor>

val client: OkHttpClient = OkHttpClient.Builder().addInterceptor(interceptor).build()
```

And add to manifest
``` xml  
<activity 
	android:name=".interceptor.InterceptorActivity" <!-- InterceptorActivity name --> 
	android:screenOrientation="portrait" 
	android:launchMode="singleTask" 
	android:taskAffinity="" 
	android:excludeFromRecents="true">
</activity>  
```

### Example
![example](example.jpg)
