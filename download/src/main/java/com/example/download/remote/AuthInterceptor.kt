package com.example.download.remote

import okhttp3.Interceptor
import okhttp3.Response

private const val token =
    "H4sIAAAAAAAAAJ1TyZKjOBT8ouoAYYryEQzGYCRZIATi0lHgskECr5jt6xu7YzpqJmYucyBE6G2Zeplfo1%2FmblHhyo%2FiyVNR5d29U6gXK%2B%2Fdk5eUrfzlj6%2FRVwvAxhToZZ7Ez%2BTxM0Fl0YRd0Kh17g4dcctu76plcSIPDpYtno4L3oQyawgIElShCbbIjXUYKXomoBLQQkMNk3DiOraRRNFzMBK5ZtWeuOTeya95Qqpg5WtFUytZ5O%2Fn%2F3OukXaforqoZoDCGeEU94geAVb9CwfrMXM%2FjjRh99z9MOB8HwhPwavFgCbvMecZUJh9IByAo8UAVwuAtT91j%2FxVtzQ%2F3aWSJ%2Btxv9JFDhRjni946ssc%2FOb6J27fB0RnTpM3Yso1aJs9VvyRp%2BiS%2F1tsaq29W4N8aglbL1exaFmsEAPaUsMJVDPhtJwWAI2KghJnfqNaIlu2CDCRUXPIGmfioljgyVERlRMUno5EMfN%2FcoonNJkACaJC2wNQwB7Se%2F%2FMxTYfkZA9sk0d0cLgIOtYXO44u2ShXKufdVnFUzh9puH1Ky2t0EE3pNT9%2F8GFbXPCdqFDO57rTSNrMskTVEPXa7nwKxQpSpaEIkh8ASl59qgRgDMPqfJJ9hhkZb5BNZ7uBpyc8Xm%2BvnHWR8MWT2FCGi%2BwfQSQmgpc9RVPmTLvZJzxX7IUVri%2BV7FqWbFsYciW8xuzyKv673cWXVse%2BRabNXvnid8VzVrmmvfUHcgSXc5Yu%2BJlCmvMQaju5zn%2F6JWSeJjP8BBKZrE1i8KnlhvU5ak18r%2Fqa0QI8wPqsEM85xCltObZf6%2F5lsOcck3j%2F44TyeLv%2BF%2BeqJdVUb18ct278ulTiRMya4XoWaRokEoQ0FDMe215AxeoUkbYOD2mWQ0pVDl9%2BU3kbl3np%2FDwOfsPVx5AgM86NvuXd204%2Fu4BW0yPOo%2FmHk9dzftETVbxBFYH8mMdM%2Bvi04Ytixu3o%2B2oIs8nJ7Rx23Zk9y9c9WwydEdob7Hmk315EL01aTo8n3v2Fhol%2Fow26ul2atbT4y1KwyT2dost9fuuvuWai9%2F8%2B%2BlqrYf03GKbDcnufeySNxSAROzsfrhdXeXS6ot9xrTYPh%2FMPO3tG06HbUg8vgFSgWO3WRERHiNyz633rWu35zL4%2BvB8x9%2F2hj3cvESNt1A6eWs3td8Nd2R3aCfeW%2FklV48pruXo19nPw44x%2FRoFiUD5BC902rOPxxDxwLkuap3mAekzh2ULw%2F6w1jeze9%2B4hnnc2NRb7jynWe3IKVPDq%2F3e6%2FTRkm1wV34C60Pf%2B%2Bch2XsEsDLM3jaegY%2B%2FAAyg15quBQAA"


class AuthInterceptor(): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()

        request.addHeader(AUTHORIZATION, token)

        return chain.proceed(request.build())
    }

    companion object {
        private const val AUTHORIZATION = "Authorization"
    }
}
