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
	
	static boolean stopCapture = false;  //����¼����־
	public static AudioFormat audioFormat;  //¼����ʽ
	    //��ȡ���ݣ���TargetDataLineд��ByteArrayOutputStream¼��
	public static  ByteArrayOutputStream byteArrayOutputStream;
	public static int totaldatasize = 0;
	static TargetDataLine targetDataLine;
	    //�������ݣ���AudioInputStreamд��SourceDataLine����
	static AudioInputStream audioInputStream;
	static SourceDataLine sourceDataLine;

    public RecordPlay() {
		//������ť
		final Button captureBtn = new Button("¼��");
		final Button stopBtn = new Button("ֹͣ");
		final Button playBtn = new Button("����");
		final Button saveBtn = new Button("����");
		captureBtn.setEnabled(true);
		stopBtn.setEnabled(false);
		playBtn.setEnabled(false);
		saveBtn.setEnabled(false);
		        //ע��¼���¼�
		captureBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				captureBtn.setEnabled(false);
				stopBtn.setEnabled(true);
				playBtn.setEnabled(false);
				saveBtn.setEnabled(false);
				//��ʼ¼��
				capture();
			}
		});
		add(captureBtn);
        //ע��ֹͣ�¼�
		stopBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				captureBtn.setEnabled(true);
				stopBtn.setEnabled(false);
				playBtn.setEnabled(true);
				saveBtn.setEnabled(true);
				//ֹͣ¼��
				stop();
			}
		});
		add(stopBtn);
        //ע�Ქ���¼�
		playBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//����¼��
				play();
			}
		});
		add(playBtn);
        //ע�ᱣ���¼�
		saveBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//����¼��
				save();
			}
		});
		add(saveBtn);
        //ע�ᴰ��ر��¼�
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
        //���ô�������
		setLayout(new FlowLayout());
		setTitle("¼��������");
		setSize(350, 70);
		setVisible(true);
		}
    	//��1��¼���¼������浽ByteArrayOutputStream��
		private void capture() {
			try {
				//��¼��
				audioFormat = getAudioFormat();
				DataLine.Info dataLineInfo = new DataLine.Info(
						TargetDataLine.class, audioFormat);
				targetDataLine = (TargetDataLine) AudioSystem.getLine(dataLineInfo);
				targetDataLine.open(audioFormat);
				targetDataLine.start();
				            //���������߳̽���¼��
				Thread captureThread = new Thread(new CaptureThread());
				captureThread.start();
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(0);
			}
		}
		//��2������ByteArrayOutputStream�е�����
		private void play() {
			try {
				//ȡ��¼������
				byte audioData[] = byteArrayOutputStream.toByteArray();
				//ת����������
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
				            //���������߳̽��в���
				Thread playThread = new Thread(new PlayThread());
				playThread.start();
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(0);
			}
		}
		    //��3��ֹͣ¼��
		public void stop() {
			stopCapture = true;
		}
		    //��4�������ļ�
		public void save() {
			//ȡ��¼��������
			AudioFormat audioFormat = getAudioFormat();
			byte audioData[] = byteArrayOutputStream.toByteArray();
			InputStream byteArrayInputStream = new ByteArrayInputStream(audioData);
			audioInputStream = new AudioInputStream(byteArrayInputStream,
			audioFormat, audioData.length / audioFormat.getFrameSize());
			//д���ļ�
			try {
				File file = new File("D:/test.wav");
				AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, file);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		    //ȡ��AudioFormat
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
