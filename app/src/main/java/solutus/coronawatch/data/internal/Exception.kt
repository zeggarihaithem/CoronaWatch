package solutus.coronawatch.data.internal

import java.io.IOException


class NoConnectivityException: IOException()
class GetDataFromApiException(message : String): IOException(message)