package com.test.audio;

class PlayThread extends Thread {
byte tempBuffer[] = new byte[10000];
    public void run() {
try {
int cnt;
//读取数据到缓存数据
while ((cnt = RecordPlay.audioInputStream.read(tempBuffer, 0,tempBuffer.length)) != -1) {
if (cnt > 0) {
  //写入缓存数据
	RecordPlay.sourceDataLine.write(tempBuffer, 0, cnt);
}
}
//Block等待临时数据被输出为空
RecordPlay.sourceDataLine.drain();
RecordPlay.sourceDataLine.close();
} catch (Exception e) {
e.printStackTrace();
System.exit(0);
}
}
}

