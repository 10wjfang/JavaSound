package com.test.audio;

class PlayThread extends Thread {
byte tempBuffer[] = new byte[10000];
    public void run() {
try {
int cnt;
//��ȡ���ݵ���������
while ((cnt = RecordPlay.audioInputStream.read(tempBuffer, 0,tempBuffer.length)) != -1) {
if (cnt > 0) {
  //д�뻺������
	RecordPlay.sourceDataLine.write(tempBuffer, 0, cnt);
}
}
//Block�ȴ���ʱ���ݱ����Ϊ��
RecordPlay.sourceDataLine.drain();
RecordPlay.sourceDataLine.close();
} catch (Exception e) {
e.printStackTrace();
System.exit(0);
}
}
}

