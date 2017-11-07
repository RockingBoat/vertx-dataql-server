package rockingboat.vertx.dataql

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.bind
import com.github.salomonbrys.kodein.singleton
import rockingboat.vertx.dataql.interfaces.IRequester

val di = Kodein {
    bind<IRequester>() with singleton { Requester() }
}