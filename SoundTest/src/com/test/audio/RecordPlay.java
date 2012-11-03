package com.test.audio;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.awt.Button;
import java.awt.Frame;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;


public class RecordPlay extends Frame {
	
	static boolean stopCapture = false;  //控制录音标志
	public static AudioFormat audioFormat;  //录音格式
	    //读取数据：从TargetDataLine写入ByteArrayOutputStream录音
	public static  ByteArrayOutputStream byteArrayOutputStream;
	public static int totaldatasize = 0;
	static TargetDataLine targetDataLine;
	    //播放数据：从AudioInputStream写入SourceDataLine播放
	static AudioInputStream audioInputStream;
	static SourceDataLine sourceDataLine;

    public RecordPlay() {
		//创建按钮
		final Button captureBtn = new Button("录音");
		final Button stopBtn = new Button("停止");
		final Button playBtn = new Button("播放");
		final Button saveBtn = new Button("保存");
		captureBtn.setEnabled(true);
		stopBtn.setEnabled(false);
		playBtn.setEnabled(false);
		saveBtn.setEnabled(false);
		        //注册录音事件
		captureBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				captureBtn.setEnabled(false);
				stopBtn.setEnabled(true);
				playBtn.setEnabled(false);
				saveBtn.setEnabled(false);
				//开始录音
				capture();
			}
		});
		add(captureBtn);
        //注册停止事件
		stopBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				captureBtn.setEnabled(true);
				stopBtn.setEnabled(false);
				playBtn.setEnabled(true);
				saveBtn.setEnabled(true);
				//停止录音
				stop();
			}
		});
		add(stopBtn);
        //注册播放事件
		playBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//播放录音
				play();
			}
		});
		add(playBtn);
        //注册保存事件
		saveBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//保存录音
				save();
			}
		});
		add(saveBtn);
        //注册窗体关闭事件
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
        //设置窗体属性
		setLayout(new FlowLayout());
		setTitle("录音机程序");
		setSize(350, 70);
		setVisible(true);
		}
    	//（1）录音事件，保存到ByteArrayOutputStream中
		private void capture() {
			try {
				//打开录音
				audioFormat = getAudioFormat();
				DataLine.Info dataLineInfo = new DataLine.Info(
						TargetDataLine.class, audioFormat);
				targetDataLine = (TargetDataLine) AudioSystem.getLine(dataLineInfo);
				targetDataLine.open(audioFormat);
				targetDataLine.start();
				            //创建独立线程进行录音
				Thread captureThread = new Thread(new CaptureThread());
				captureThread.start();
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(0);
			}
		}
		//（2）播放ByteArrayOutputStream中的数据
		private void play() {
			try {
				//取得录音数据
				byte audioData[] = byteArrayOutputStream.toByteArray();
				//转换成输入流
				InputStream byteArrayInputStream = new ByteArrayInputStream(
				audioData);
				AudioFormat audioFormat = getAudioFormat();
				audioInputStream = new AudioInputStream(byteArrayInputStream,
				audioFormat, audioData.length / audioFormat.getFrameSize());
				DataLine.Info dataLineInfo = new DataLine.Info(
				SourceDataLine.class, audioFormat);
				sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
				sourceDataLine.open(audioFormat);
				sourceDataLine.start();
				            //创建独立线程进行播放
				Thread playThread = new Thread(new PlayThread());
				playThread.start();
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(0);
			}
		}
		    //（3）停止录音
		public void stop() {
			stopCapture = true;
		}
		    //（4）保存文件
		public void save() {
			//取得录音输入流
			AudioFormat audioFormat = getAudioFormat();
			byte audioData[] = byteArrayOutputStream.toByteArray();
			InputStream byteArrayInputStream = new ByteArrayInputStream(audioData);
			audioInputStream = new AudioInputStream(byteArrayInputStream,
			audioFormat, audioData.length / audioFormat.getFrameSize());
			//写入文件
			try {
				File file = new File("D:/test.wav");
				AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, file);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		    //取得AudioFormat
		private AudioFormat getAudioFormat() {
			float sampleRate = 16000.0F;
			//8000,11025,16000,22050,44100
			int sampleSizeInBits = 16;
			//8,16
			int channels = 1;
			//1,2
			boolean signed = true;
			//true,false
			boolean bigEndian = false;
			//true,false
			return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed,
			bigEndian);
		}
		public static void main(String args[]) {
			new RecordPlay();
		}
}

