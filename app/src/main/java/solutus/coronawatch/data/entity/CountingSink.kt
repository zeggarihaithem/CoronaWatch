package solutus.coronawatch.data.entity

import okhttp3.RequestBody
import okio.Buffer
import okio.ForwardingSink
import okio.Sink

typealias CountingRequestListener = (bytesWritten: Long, contentLength: Long) -> Unit

class CountingSink(
    sink: Sink,
    private val requestBody: RequestBody,
    private val onProgressUpdate: CountingRequestListener
) : ForwardingSink(sink) {
    private var bytesWritten = 0L

    override fun write(source: Buffer, byteCount: Long) {
        super.write(source, byteCount)

        bytesWritten += byteCount
        onProgressUpdate(bytesWritten, requestBody.contentLength())
    }
}