package com.acalidonio.bodegamovil.data.remote

class ServerApiException(val serverMessage: String) : Exception(serverMessage)
