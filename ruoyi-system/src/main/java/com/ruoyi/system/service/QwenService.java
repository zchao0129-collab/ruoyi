package com.ruoyi.system.service;

import com.ruoyi.common.core.domain.AjaxResult;

import java.io.IOException;
import java.net.ProtocolException;

public interface QwenService {

    AjaxResult getShortAnswer(String question);

    AjaxResult getDetailedAnswer(String question);

    AjaxResult getAnswer(String question) throws IOException;
}
