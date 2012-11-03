package com.test.audio;

import java.io.ByteArrayOutputStream;


class CaptureThread extends Thread {
	//临时数组
  	byte tempBuffer[] = new byte[10000];
	    public void run() {
	RecordPlay.byteArrayOutputStream = new ByteArrayOutputStream();
	RecordPlay.totaldatasize = 0;
	RecordPlay.stopCapture = false;
	try {//循环执行，直到按下停止录音按钮
	while (!RecordPlay.stopCapture) {
	//读取10000个数据
	int cnt = RecordPlay.targetDataLine.read(tempBuffer, 0,
	tempBuffer.length);
	if (cnt > 0) {
	//保存该数据
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

