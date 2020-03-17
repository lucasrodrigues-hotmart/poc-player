package com.hotmart.sparkle.pocplayer

import android.util.Log
import okhttp3.*
import java.io.IOException
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.Proxy
import kotlin.time.ExperimentalTime
import kotlin.time.MonoClock

@ExperimentalTime
class CustomEventListener : EventListener() {
    private val clock = MonoClock.markNow()

    override fun callEnd(call: Call) {
        super.callEnd(call)
        Log.e("Timer-OkHttp", "call end: ${clock.elapsedNow()}")
    }

    override fun callFailed(call: Call, ioe: IOException) {
        super.callFailed(call, ioe)
        Log.e("Timer-OkHttp", "call failed: ${clock.elapsedNow()}")
    }

    override fun callStart(call: Call) {
        super.callStart(call)
        Log.e("Timer-OkHttp", "call start: ${clock.elapsedNow()}")
    }

    override fun connectEnd(
        call: Call,
        inetSocketAddress: InetSocketAddress,
        proxy: Proxy,
        protocol: Protocol?
    ) {
        super.connectEnd(call, inetSocketAddress, proxy, protocol)
        Log.e("Timer-OkHttp", "connect end: ${clock.elapsedNow()}")
    }

    override fun connectFailed(
        call: Call,
        inetSocketAddress: InetSocketAddress,
        proxy: Proxy,
        protocol: Protocol?,
        ioe: IOException
    ) {
        super.connectFailed(call, inetSocketAddress, proxy, protocol, ioe)
        Log.e("Timer-OkHttp", "connect failed: ${clock.elapsedNow()}")
    }

    override fun connectStart(call: Call, inetSocketAddress: InetSocketAddress, proxy: Proxy) {
        super.connectStart(call, inetSocketAddress, proxy)
        Log.e("Timer-OkHttp", "connect start: ${clock.elapsedNow()}")
    }

    override fun connectionAcquired(call: Call, connection: Connection) {
        super.connectionAcquired(call, connection)
        Log.e("Timer-OkHttp", "connection acquired: ${clock.elapsedNow()}")
    }

    override fun connectionReleased(call: Call, connection: Connection) {
        super.connectionReleased(call, connection)
        Log.e("Timer-OkHttp", "connection released: ${clock.elapsedNow()}")
    }

    override fun dnsEnd(call: Call, domainName: String, inetAddressList: List<InetAddress>) {
        super.dnsEnd(call, domainName, inetAddressList)
        Log.e("Timer-OkHttp", "dns end: ${clock.elapsedNow()}")
    }

    override fun dnsStart(call: Call, domainName: String) {
        super.dnsStart(call, domainName)
        Log.e("Timer-OkHttp", "dns start: ${clock.elapsedNow()}")
    }

    override fun proxySelectEnd(call: Call, url: HttpUrl, proxies: List<Proxy>) {
        super.proxySelectEnd(call, url, proxies)
        Log.e("Timer-OkHttp", "proxy select end: ${clock.elapsedNow()}")
    }

    override fun proxySelectStart(call: Call, url: HttpUrl) {
        super.proxySelectStart(call, url)
        Log.e("Timer-OkHttp", "proxy select start: ${clock.elapsedNow()}")
    }

    override fun requestBodyEnd(call: Call, byteCount: Long) {
        super.requestBodyEnd(call, byteCount)
        Log.e("Timer-OkHttp", "request body end: ${clock.elapsedNow()}")
    }

    override fun requestBodyStart(call: Call) {
        super.requestBodyStart(call)
        Log.e("Timer-OkHttp", "request body start: ${clock.elapsedNow()}")
    }

    override fun requestFailed(call: Call, ioe: IOException) {
        super.requestFailed(call, ioe)
        Log.e("Timer-OkHttp", "request body failed: ${clock.elapsedNow()}")
    }

    override fun requestHeadersEnd(call: Call, request: Request) {
        super.requestHeadersEnd(call, request)
        Log.e("Timer-OkHttp", "request headers end: ${clock.elapsedNow()}")
    }

    override fun requestHeadersStart(call: Call) {
        super.requestHeadersStart(call)
        Log.e("Timer-OkHttp", "request headers start: ${clock.elapsedNow()}")
    }

    override fun responseBodyEnd(call: Call, byteCount: Long) {
        super.responseBodyEnd(call, byteCount)
        Log.e("Timer-OkHttp", "response body end: ${clock.elapsedNow()}")
    }

    override fun responseBodyStart(call: Call) {
        super.responseBodyStart(call)
        Log.e("Timer-OkHttp", "response body start: ${clock.elapsedNow()}")
    }

    override fun responseFailed(call: Call, ioe: IOException) {
        super.responseFailed(call, ioe)
        Log.e("Timer-OkHttp", "response body failed: ${clock.elapsedNow()}")
    }

    override fun responseHeadersEnd(call: Call, response: Response) {
        super.responseHeadersEnd(call, response)
        Log.e("Timer-OkHttp", "response headers end: ${clock.elapsedNow()}")
    }

    override fun responseHeadersStart(call: Call) {
        super.responseHeadersStart(call)
        Log.e("Timer-OkHttp", "response headers start: ${clock.elapsedNow()}")
    }

    override fun secureConnectEnd(call: Call, handshake: Handshake?) {
        super.secureConnectEnd(call, handshake)
        Log.e("Timer-OkHttp", "secure connect end: ${clock.elapsedNow()}")
    }

    override fun secureConnectStart(call: Call) {
        super.secureConnectStart(call)
        Log.e("Timer-OkHttp", "secure connect start: ${clock.elapsedNow()}")
    }
}
