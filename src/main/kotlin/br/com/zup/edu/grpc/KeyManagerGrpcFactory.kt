package br.com.zup.edu.grpc

import br.com.zup.edu.KeyConsultServiceGrpc
import br.com.zup.edu.KeyListServiceGrpc
import br.com.zup.edu.KeyManagerServiceGrpc
import br.com.zup.edu.KeyRemoveServiceGrpc
import io.grpc.ManagedChannel
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import javax.inject.Singleton

@Factory
class KeyManagerGrpcFactory(@GrpcChannel("keyManager") val channel: ManagedChannel) {

    @Singleton
    fun registerKey() = KeyManagerServiceGrpc.newBlockingStub(channel)

    @Singleton
    fun removeKey() = KeyRemoveServiceGrpc.newBlockingStub(channel)

    @Singleton
    fun listKey() = KeyListServiceGrpc.newBlockingStub(channel)

    @Singleton
    fun consultKey() = KeyConsultServiceGrpc.newBlockingStub(channel)

}