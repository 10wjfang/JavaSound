package com.test.audio;

import java.io.ByteArrayOutputStream;


class CaptureThread extends Thread {
	//��ʱ����
  	byte tempBuffer[] = new byte[10000];
	    public void run() {
	RecordPlay.byteArrayOutputStream = new ByteArrayOutputStream();
	RecordPlay.totaldatasize = 0;
	RecordPlay.stopCapture = false;
	try {//ѭ��ִ�У�ֱ������ֹͣ¼����ť
	while (!RecordPlay.stopCapture) {
	//��ȡ10000������
	int cnt = RecordPlay.targetDataLine.read(tempBuffer, 0,
	tempBuffer.length);
	if (cnt > 0) {
	//���������
		RecordPlay.byteArrayOutputStream.write(tempBuffer, 0, cnt);
		RecordPlay.totaldatasize += cnt;
	}
	}
	RecordPlay.byteArrayOutputStream.close();
	} catch (Exception e) {
	e.printStackTrace();
	System.exit(0);
	}
	}
	}

