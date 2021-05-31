package br.com.zup.edu.grpc

import br.com.zup.edu.*
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