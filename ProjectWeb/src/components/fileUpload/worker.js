import SparkMD5 from 'spark-md5'

const getFileMD5 = (file, index, chunkSize) => {
    return new Promise((resolve, reject) => {
        const suffix = /\.([0-9A-z]+)$/.exec(file.name)[1]
        const start = index * chunkSize
        const end=start+chunkSize
        const blob=file.slice(start,end)
        const spark = new SparkMD5.ArrayBuffer()
        const fr = new FileReader()
        fr.onload = (e) => {
            spark.append(e.target.result)
            let fileHash=spark.end()
            resolve({
                chunk:blob,
                fileName: `${fileHash}.${suffix}`,
                index,
                start,
                end,
                fileHash
            })
            postMessage(index)
        }
        fr.readAsArrayBuffer(blob)
        fr.onerror = (e) => {
            reject(new Error('转换文件格式发生错误'))
        }
    })
}

onmessage = async (e) => {
    const { file, start, end, chunkSize } = e.data
    let result = []
    for (let i = start; i < end; i++) {
        const res=getFileMD5(file,i,chunkSize)
        result.push(res)
    }
    const chunks=await Promise.all(result)
    postMessage(chunks)
}