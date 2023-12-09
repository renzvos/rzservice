package com.renzvos.rservice.errors

class NoClassName : Exception() {
}

class MultipleService_SameName : Exception()
{

}

class PeriodicWorkNotFound : Exception()
{}

class CoroutineWorkNotFound : Exception()
{}

class UnknownTaginRZWorker (tagName : String) : Exception( "Unknown Tag " + tagName){}

class RZWorkerNotFound : Exception() {}