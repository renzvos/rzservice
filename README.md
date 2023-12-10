# RENZVOS
## Android Background Tool (KOTLIN)

You gotta install and add dependency , details will be given at the end

OK, We need to run something in background

### Lets make some code run in background

First We make a class run in background -- For that we just extend it with RZWorker .

Watch

    class MessageCheckingService(appContext: Context , params : WorkerParameters) : RZWorker(appContext , params )  
    {}  




Now you would be asked to do an implementation - After implementation it would look like this  
BAM!


    class MessageCheckingService(appContext: Context, params : WorkerParameters) : RZWorker(appContext , params )  
    {  
	    override suspend fun work() {  
		    TODO("Not yet implemented")  
	    }  
    }  


You put your code inside that work function .

### NOWW.... you need to say when you should start/end and stuff

#### How to start?


>  For Continous Work (Autowaking Means - It reminds to come alive even
> after its killed)



    val service = RZService.new("your-name-for-service", 
							    MessageCheckingService::class.java, // Your Code
							    applicationContext)
							    .autowaking_continous_work()  




> and for Periodic Work

    val service = RZService.new("your-name-for-service", MessageCheckingService::class.java, applicationContext).periodic_work()  


AND  .. Do this to start - (You dont have to worry if its already started)

    service.start()

and do this to kill


    service.kill()

and there is more ..



## Installation

Add these dependency in your build.gradle.kts (App level)

    dependencies {  
	    implementation("")  
	    implementation("androidx.work:work-runtime-ktx:2.9.0")  
    }  
