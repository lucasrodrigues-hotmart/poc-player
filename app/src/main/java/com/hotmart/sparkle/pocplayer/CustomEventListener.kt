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
class CustomEventListener(var isLogEnabled: Boolean = false, private val tag: String) : EventListener() {
    private var clock = MonoClock.markNow()

    fun resetTimer() {
        clock = MonoClock.markNow()
    }

    override fun callEnd(call: Call) {
        super.callEnd(call)
        if (isLogEnabled) {
            Log.e(tag, "call end: ${clock.elapsedNow()}")
        }
    }

    override fun callFailed(call: Call, ioe: IOException) {
        super.callFailed(call, ioe)
        if (isLogEnabled) {
            Log.e(tag, "call failed: ${clock.elapsedNow()}")
        }
    }

    override fun callStart(call: Call) {
        super.callStart(call)
        if (isLogEnabled) {
            Log.e(tag, "call start: ${clock.elapsedNow()}")
        }
    }

    override fun connectEnd(
        call: Call,
        inetSocketAddress: InetSocketAddress,
        proxy: Proxy,
        protocol: Protocol?
    ) {
        super.connectEnd(call, inetSocketAddress, proxy, protocol)
        if (isLogEnabled) {
            Log.e(tag, "connect end: ${clock.elapsedNow()}")
        }
    }

    override fun connectFailed(
        call: Call,
        inetSocketAddress: InetSocketAddress,
        proxy: Proxy,
        protocol: Protocol?,
        ioe: IOException
    ) {
        super.connectFailed(call, inetSocketAddress, proxy, protocol, ioe)
        if (isLogEnabled) {
            Log.e(tag, "connect failed: ${clock.elapsedNow()}")
        }
    }

    override fun connectStart(call: Call, inetSocketAddress: InetSocketAddress, proxy: Proxy) {
        super.connectStart(call, inetSocketAddress, proxy)
        if (isLogEnabled) {
            Log.e(tag, "connect start: ${clock.elapsedNow()}")
        }
    }

    override fun connectionAcquired(call: Call, connection: Connection) {
        super.connectionAcquired(call, connection)
        if (isLogEnabled) {
            Log.e(tag, "connection acquired: ${clock.elapsedNow()}")
        }
    }

    override fun connectionReleased(call: Call, connection: Connection) {
        super.connectionReleased(call, connection)
        if (isLogEnabled) {
            Log.e(tag, "connection released: ${clock.elapsedNow()}")
        }
    }

    override fun dnsEnd(call: Call, domainName: String, inetAddressList: List<InetAddress>) {
        super.dnsEnd(call, domainName, inetAddressList)
        if (isLogEnabled) {
            Log.e(tag, "dns end: ${clock.elapsedNow()}")
        }
    }

    override fun dnsStart(call: Call, domainName: String) {
        super.dnsStart(call, domainName)
        if (isLogEnabled) {
            Log.e(tag, "dns start: ${clock.elapsedNow()}")
        }
    }

    override fun proxySelectEnd(call: Call, url: HttpUrl, proxies: List<Proxy>) {
        super.proxySelectEnd(call, url, proxies)
        if (isLogEnabled) {
            Log.e(tag, "proxy select end: ${clock.elapsedNow()}")
        }
    }

    override fun proxySelectStart(call: Call, url: HttpUrl) {
        super.proxySelectStart(call, url)
        if (isLogEnabled) {
            Log.e(tag, "proxy select start: ${clock.elapsedNow()}")
        }
    }

    override fun requestBodyEnd(call: Call, byteCount: Long) {
        super.requestBodyEnd(call, byteCount)
        if (isLogEnabled) {
            Log.e(tag, "request body end: ${clock.elapsedNow()}")
        }
    }

    override fun requestBodyStart(call: Call) {
        super.requestBodyStart(call)
        if (isLogEnabled) {
            Log.e(tag, "request body start: ${clock.elapsedNow()}")
        }
    }

    override fun requestFailed(call: Call, ioe: IOException) {
        super.requestFailed(call, ioe)
        if (isLogEnabled) {
            Log.e(tag, "request body failed: ${clock.elapsedNow()}")
        }
    }

    override fun requestHeadersEnd(call: Call, request: Request) {
        super.requestHeadersEnd(call, request)
        if (isLogEnabled) {
            Log.e(tag, "request headers end: ${clock.elapsedNow()}")
        }
    }

    override fun requestHeadersStart(call: Call) {
        super.requestHeadersStart(call)
        if (isLogEnabled) {
            Log.e(tag, "request headers start: ${clock.elapsedNow()}")
        }
    }

    override fun responseBodyEnd(call: Call, byteCount: Long) {
        super.responseBodyEnd(call, byteCount)
        if (isLogEnabled) {
            Log.e(tag, "response body end: ${clock.elapsedNow()}")
        }
    }

    override fun responseBodyStart(call: Call) {
        super.responseBodyStart(call)
        if (isLogEnabled) {
            Log.e(tag, "response body start: ${clock.elapsedNow()}")
        }
    }

    override fun responseFailed(call: Call, ioe: IOException) {
        super.responseFailed(call, ioe)
        if (isLogEnabled) {
            Log.e(tag, "response body failed: ${clock.elapsedNow()}")
        }
    }

    override fun responseHeadersEnd(call: Call, response: Response) {
        super.responseHeadersEnd(call, response)
        if (isLogEnabled) {
            Log.e(tag, "response headers end: ${clock.elapsedNow()}")
        }
    }

    override fun responseHeadersStart(call: Call) {
        super.responseHeadersStart(call)
        if (isLogEnabled) {
            Log.e(tag, "response headers start: ${clock.elapsedNow()}")
        }
    }

    override fun secureConnectEnd(call: Call, handshake: Handshake?) {
        super.secureConnectEnd(call, handshake)
        if (isLogEnabled) {
            Log.e(tag, "secure connect end: ${clock.elapsedNow()}")
        }
    }

    override fun secureConnectStart(call: Call) {
        super.secureConnectStart(call)
        if (isLogEnabled) {
            Log.e(tag, "secure connect start: ${clock.elapsedNow()}")
        }
    }
}
