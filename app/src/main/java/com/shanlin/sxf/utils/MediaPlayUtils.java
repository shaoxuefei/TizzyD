package com.shanlin.sxf.utils;

import android.media.MediaPlayer;
import android.media.SyncParams;
import android.os.Environment;
import android.widget.Toast;

import java.io.IOException;

/**
 * @author : SXF
 * @ date   : 2018/12/17
 * Description :
 */
public class MediaPlayUtils {
    MediaPlayer mediaPlayer;

    public MediaPlayUtils() {
        initMediaPlay();
    }

    public void initMediaPlay() {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setVolume(300, 300);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            SyncParams syncParams = new SyncParams();
        }

//        speechSynthesizer.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
//        speechSynthesizer.setParameter(SpeechConstant.TTS_DATA_NOTIFY, "1");
//        // 设置在线合成发音人
//        speechSynthesizer.setParameter(SpeechConstant.VOICE_NAME, "xiaoyan");
//        //设置合成语速
//        speechSynthesizer.setParameter(SpeechConstant.SPEED, "50");
//        //设置合成音调
//        speechSynthesizer.setParameter(SpeechConstant.PITCH, "50");
//        //设置合成音量
//        speechSynthesizer.setParameter(SpeechConstant.VOLUME, "100");
//        //设置播放器音频流类型
//        speechSynthesizer.setParameter(SpeechConstant.STREAM_TYPE, "3");
//        // 设置播放合成音频打断音乐播放，默认为true
//        speechSynthesizer.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, "true");
    }

    public void startPlay(String content) {
        if (mediaPlayer.isPlaying()) {
//            mediaPlayer.reset();
//            initMediaPlay();
        } else if (!mediaPlayer.isPlaying() && isPause) {
            mediaPlayer.start();
            isPause = false;
        } else {
            mediaPlayer.reset();
            initMediaPlay();
            try {
                mediaPlayer.setDataSource(content);
                mediaPlayer.prepareAsync();
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mp.start();
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    boolean isPause;

    public void onPause() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            isPause = true;
        }
    }


    public void onDestory() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.reset();
            mediaPlayer.stop();
        }
    }
}
