package com.app.intstreetlight.logic.net

import com.huaweicloud.sdk.core.auth.BasicCredentials
import com.huaweicloud.sdk.core.auth.ICredential
import com.huaweicloud.sdk.iotda.v5.IoTDAClient
import com.huaweicloud.sdk.iotda.v5.region.IoTDARegion


object Client {



    fun iotClient(): IoTDAClient {
        val ak = "69W1S9K4PXTMSXF9TVDM"
        val sk = "BcgYaUtsJijJLkT3n8ZLO7xGDxuWblCi5NTVeCbr"
        val auth: ICredential = BasicCredentials()
            .withAk(ak)
            .withSk(sk)
        return IoTDAClient.newBuilder()
            .withCredential(auth)
            .withRegion(IoTDARegion.CN_NORTH_4)
            .build()
    }


}