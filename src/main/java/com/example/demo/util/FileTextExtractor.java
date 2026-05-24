// 파일 텍스트 변환기 입니다/ =======================================================
package com.example.demo.util;

import org.springframework.stereotype.Component;
import java.io.InputStream;import org.apache.tika.Tika;

@Component
public class FileTextExtractor {
    public String extractText(InputStream inputStream) throws Exception {
        Tika tika = new Tika();
        return tika.parseToString(inputStream); // 파일 종류 상관없이 글자만 쏙 뽑아줌
    }
}