package com.ruoyi.system.uwqkejicn;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.security.XRSAUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.*;
//import net.sf.json.JSONObject;

public class Hzxj_demo {

	public static void main(String[] args) {
		//payorder(); //下單
		//rorder();//退款
		//qorder();//查詢
	}

	//下單
	public static void payorder(){
		String key="dDWkfXIw7Dak6FFMfxZkNNp77gxzpgGG";
		String url="https://shopapi.wqkeji.cn/";
		String publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA0IdduGxn1ZXZpEL0d2W9 MR7/g0fFE7elS0cabE0nVp7Mbw3jU+xAL/GlkSpicb4hXnGzLoi8asxUAsUnbuBr SAaumic0eKaiHMzeXJFr9zce4UkudxOfxqKknRJjFsXq1+vaKIKmdSpsLia1neKb ncuw2aLQS/FYbt8/2lerI5ax07t9HQ5soOz5dG2CMy68A8rqg6dIxHRpTTGa+7om k7twgPGVpFLCaS0IB2fAZ2RyIbhv/i8BKBFjSzjZQC3iGMU5IqWm8zVynkh4/M5N Nj5fedh+Q9VZtkapeQRC+7g/R2qPySX3JVTt/VzjFUHWhAVPdkykn5ruv0I3ImjF 8wIDAQAB";
		String passPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAn0mGQ1S8+yw6LA+ZLTrv 2gS0vsWxpvaDRjkI44WK8aqefeFqrPDNV7YXXKmEqiZNxd/J6TzLtVUYDOWsf/CH OL9r0xb4OdaShthU4BvKd1j+XPr9wk2gYSe/ECJU2KyxBsvSfYv2HsPbklexNoFx +iJ/vgmTtktf0yj74wa2+R2PNUeBKUW8wY422fC/hcuLIvIlANTSrb35bp7bk8ZN A8w0HLqkZCxAQRqs0ore9PoJy4P3CA7xs6UngSNFmf/9+rtbMdkCTqMjoHFg+qtw YeWvvgE8orXq+s4q19fyFXWcSypNgrnuyaG2xf8dbphOXmeh5/hBvcCCMHmFBdu3 qQIDAQAB";
		String privateKey = "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQDQh124bGfVldmk QvR3Zb0xHv+DR8UTt6VLRxpsTSdWnsxvDeNT7EAv8aWRKmJxviFecbMuiLxqzFQC xSdu4GtIBq6aJzR4pqIczN5ckWv3Nx7hSS53E5/GoqSdEmMWxerX69oogqZ1Kmwu JrWd4pudy7DZotBL8Vhu3z/aV6sjlrHTu30dDmyg7Pl0bYIzLrwDyuqDp0jEdGlN MZr7uiaTu3CA8ZWkUsJpLQgHZ8BnZHIhuG/+LwEoEWNLONlALeIYxTkipabzNXKe SHj8zk02Pl952H5D1Vm2Rql5BEL7uD9Hao/JJfclVO39XOMVQdaEBU92TKSfmu6/ QjciaMXzAgMBAAECggEBAMbaT2Uu+1WSMJmTv8ycGG5fQQnPxv+mgNnoVCoy6PCR PiIo/D9ra0pNBbYI1RfiV6/YfSZC/mpZr0N0/sWc/LESri4SdNcZED6mnuVnR63X wnrTbpve+t5Q+ulOP2/YDLsbDk7TSCky6zaXc1m8Xul69IqV2EyTOeGAw0NTlr+3 mrhF25Z6jmfntwhYmkMGFsifpYjQCB4AX2TVDxeLZQ1EG9aNSehKviqaqbj0L9+Y /0o9ExO3aK56P2oBkQDdaszvaeybe4aHun87fJ9cMekYXt/V3lo/RmLjwODW9uwm 3Zp2A5gJI72J6BqNHs2HMF63RDygkK5R2ZihlH3FpAECgYEA+PceI0hWsYQMr82T V9COdUqin9C4Byu9pLJ89NpUtqL9TnTwCF4E/+0h6G/1H0TCr1jjlR5SHvfylesl jMEiHqC8FgF7ppP0/J3BbC+8aOz66cMmWaTv+9ML16J8/C8qnoaKu1AwFiMtv+GU w9UM6izuf5LFvd+PxIGhh7H0zpECgYEA1mvAh7lcvbltNBa7Fr25uzGHRF9ZGY89 ypzZqb/DkSJA6fdjmR1Sc7+11n/coybKRO8fMEUlH6P7vzOBCJYwJ/cTh687qfta 71qe7HH/mc+ULDWgt37sDJvhaEyqTwtZjI/FGPhRhXyoRWwtmwM61Jcn+LRRf0RL GYzzeW/SVkMCgYBzBI9601+h0RSfX4Tzq9k0nM92r9FlFrMdVIxf/kHmwnPt/B4s b2s15RKmNJr04CplIda6PHDJ99itpB4L4enmZnGeZP8AHRiwYB1M4tzTSLA+sLaV 8ZohKAYvj9TYGN5CsHuFP+2HPKdVH10//7xgr3Nboq4SIydjLFQ4wWEM0QKBgQDD hsqweXV6hMZyQ3Ajkqr1anrA/HmRdy+PvWw3AOljRWsQb0lhbH2tk1pL2ZjnJg44 wh0ctVDfA3gjqOG3ZM1RBFvI1SCBJvaLYYWec23zYnjpv5Vo/tJdxR/NLmv3XJAy P0AqPvaH1V7Uv4ru2gkhpx0KC9RtS2d4SzISwMd7ZQKBgQC4XzFuJyENHQgUc4kD uKzfIPgPzXFblu9MKdxA4YdOrQeOAL68kUAkXOL5bc+Gv6roideXU/dnpuhwd5B0 NvcE/hy2bSE0RCwHkxWyK0d5CVVaLwvitb6Em6dAonH2AcLTlM2ZSEN/6GKRiTYh EQvfX5pds0zycM76KKYeO0eexw==";

		SortedMap<String, String> map = new TreeMap<String, String>();
		map.put("service","App.Order.Pay");
		map.put("timestamp", DateUtils.getTime());
		map.put("nonce",generateNonceStr());
		map.put("member_id","220608655");
		map.put("mem_order", UUID.randomUUID().toString());
		map.put("notifyurl","www.baidu.com");
		map.put("callbackurl","www.baidu.com");
		map.put("amount","0.01");
		map.put("productname","测试");
		map.put("bank_code","953");

		String sign = mapToStringAndTrimForNewSys(map)+key;
		sign = md5encode(sign).toUpperCase();
		map.put("sign",sign);
		XRSAUtil xRSAUtil=new XRSAUtil(publicKey, privateKey);
		String notifyurlRSA=xRSAUtil.privateEncrypt(map.get("notifyurl"));
		System.out.println(map.get("notifyurl")+" 加密后:"+notifyurlRSA);
		map.put("notifyurl",notifyurlRSA);
		System.out.println(" 解密后:"+xRSAUtil.publicDecrypt(notifyurlRSA));
		String retMsg = HttpUtil.post(url,mapToStringAndTrim(map));
		//JSONObject json = JSONObject.fromObject(retMsg);
		JSONObject json = JSONObject.parseObject(retMsg);
		if("200".equals(json.getString("ret"))){
			String data = json.getString("data");
			XRSAUtil xRSAUtil1=new XRSAUtil(passPublicKey, privateKey);
			String dataResponse=xRSAUtil1.publicDecrypt(data);
			System.out.println("dataResponse: "+dataResponse);
			JSONObject resDataJson = JSONObject.parseObject(dataResponse);
			System.out.println("orderid: "+resDataJson.getString("orderid"));
			System.out.println("pay_url: "+resDataJson.getString("pay_url"));
		}
	}
	//退款
	public static void rorder() throws IOException {
		String key="dDWkfXIw7Dak6FFMfxZkNNp77gxzpgGG";
		String url="https://shopapi.wqkeji.cn/";
		String publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA0IdduGxn1ZXZpEL0d2W9 MR7/g0fFE7elS0cabE0nVp7Mbw3jU+xAL/GlkSpicb4hXnGzLoi8asxUAsUnbuBr SAaumic0eKaiHMzeXJFr9zce4UkudxOfxqKknRJjFsXq1+vaKIKmdSpsLia1neKb ncuw2aLQS/FYbt8/2lerI5ax07t9HQ5soOz5dG2CMy68A8rqg6dIxHRpTTGa+7om k7twgPGVpFLCaS0IB2fAZ2RyIbhv/i8BKBFjSzjZQC3iGMU5IqWm8zVynkh4/M5N Nj5fedh+Q9VZtkapeQRC+7g/R2qPySX3JVTt/VzjFUHWhAVPdkykn5ruv0I3ImjF 8wIDAQAB";
		String passPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAn0mGQ1S8+yw6LA+ZLTrv 2gS0vsWxpvaDRjkI44WK8aqefeFqrPDNV7YXXKmEqiZNxd/J6TzLtVUYDOWsf/CH OL9r0xb4OdaShthU4BvKd1j+XPr9wk2gYSe/ECJU2KyxBsvSfYv2HsPbklexNoFx +iJ/vgmTtktf0yj74wa2+R2PNUeBKUW8wY422fC/hcuLIvIlANTSrb35bp7bk8ZN A8w0HLqkZCxAQRqs0ore9PoJy4P3CA7xs6UngSNFmf/9+rtbMdkCTqMjoHFg+qtw YeWvvgE8orXq+s4q19fyFXWcSypNgrnuyaG2xf8dbphOXmeh5/hBvcCCMHmFBdu3 qQIDAQAB";
		String privateKey = "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQDQh124bGfVldmk QvR3Zb0xHv+DR8UTt6VLRxpsTSdWnsxvDeNT7EAv8aWRKmJxviFecbMuiLxqzFQC xSdu4GtIBq6aJzR4pqIczN5ckWv3Nx7hSS53E5/GoqSdEmMWxerX69oogqZ1Kmwu JrWd4pudy7DZotBL8Vhu3z/aV6sjlrHTu30dDmyg7Pl0bYIzLrwDyuqDp0jEdGlN MZr7uiaTu3CA8ZWkUsJpLQgHZ8BnZHIhuG/+LwEoEWNLONlALeIYxTkipabzNXKe SHj8zk02Pl952H5D1Vm2Rql5BEL7uD9Hao/JJfclVO39XOMVQdaEBU92TKSfmu6/ QjciaMXzAgMBAAECggEBAMbaT2Uu+1WSMJmTv8ycGG5fQQnPxv+mgNnoVCoy6PCR PiIo/D9ra0pNBbYI1RfiV6/YfSZC/mpZr0N0/sWc/LESri4SdNcZED6mnuVnR63X wnrTbpve+t5Q+ulOP2/YDLsbDk7TSCky6zaXc1m8Xul69IqV2EyTOeGAw0NTlr+3 mrhF25Z6jmfntwhYmkMGFsifpYjQCB4AX2TVDxeLZQ1EG9aNSehKviqaqbj0L9+Y /0o9ExO3aK56P2oBkQDdaszvaeybe4aHun87fJ9cMekYXt/V3lo/RmLjwODW9uwm 3Zp2A5gJI72J6BqNHs2HMF63RDygkK5R2ZihlH3FpAECgYEA+PceI0hWsYQMr82T V9COdUqin9C4Byu9pLJ89NpUtqL9TnTwCF4E/+0h6G/1H0TCr1jjlR5SHvfylesl jMEiHqC8FgF7ppP0/J3BbC+8aOz66cMmWaTv+9ML16J8/C8qnoaKu1AwFiMtv+GU w9UM6izuf5LFvd+PxIGhh7H0zpECgYEA1mvAh7lcvbltNBa7Fr25uzGHRF9ZGY89 ypzZqb/DkSJA6fdjmR1Sc7+11n/coybKRO8fMEUlH6P7vzOBCJYwJ/cTh687qfta 71qe7HH/mc+ULDWgt37sDJvhaEyqTwtZjI/FGPhRhXyoRWwtmwM61Jcn+LRRf0RL GYzzeW/SVkMCgYBzBI9601+h0RSfX4Tzq9k0nM92r9FlFrMdVIxf/kHmwnPt/B4s b2s15RKmNJr04CplIda6PHDJ99itpB4L4enmZnGeZP8AHRiwYB1M4tzTSLA+sLaV 8ZohKAYvj9TYGN5CsHuFP+2HPKdVH10//7xgr3Nboq4SIydjLFQ4wWEM0QKBgQDD hsqweXV6hMZyQ3Ajkqr1anrA/HmRdy+PvWw3AOljRWsQb0lhbH2tk1pL2ZjnJg44 wh0ctVDfA3gjqOG3ZM1RBFvI1SCBJvaLYYWec23zYnjpv5Vo/tJdxR/NLmv3XJAy P0AqPvaH1V7Uv4ru2gkhpx0KC9RtS2d4SzISwMd7ZQKBgQC4XzFuJyENHQgUc4kD uKzfIPgPzXFblu9MKdxA4YdOrQeOAL68kUAkXOL5bc+Gv6roideXU/dnpuhwd5B0 NvcE/hy2bSE0RCwHkxWyK0d5CVVaLwvitb6Em6dAonH2AcLTlM2ZSEN/6GKRiTYh EQvfX5pds0zycM76KKYeO0eexw==";
		XRSAUtil xRSAUtil=new XRSAUtil(publicKey, privateKey);
		SortedMap<String, String> map = new TreeMap<String, String>();
		map.put("service","App.Order.Refund");//接口名(固定)
		map.put("timestamp",DateUtils.getTime());//时间格式:2022-01-01 00:00:00
		map.put("nonce", Hzxj_demo.generateNonceStr());//唯一随机字符串
		map.put("member_id","220608655");//商户ID(系统分配)
		map.put("order_id","220608655OR37D874A1D4286EEF20230104153743");//平台订单号(RSA加密,先签名再加密)
		map.put("remoney","0");//退款金额(0或不传为全额退款)
		String sign = Hzxj_demo.mapToStringAndTrimForNewSys(map)+key;
		sign = Hzxj_demo.md5encode(sign).toUpperCase();
		map.put("sign",sign);//签名(不参与签名)
		String orderIdRSA=xRSAUtil.privateEncrypt(map.get("order_id"));
		map.put("order_id",orderIdRSA);
		System.out.println("订单退款参数:"+Hzxj_demo.mapToStringAndTrim(map));
		String res =Hzxj_demo.sendPost(url, Hzxj_demo.mapToStringAndTrim(map),"utf-8");
		String dataStr = "";
		JSONObject json = JSONObject.parseObject(res);
		XRSAUtil jRSAUtil=new XRSAUtil(passPublicKey, privateKey);
		if("200".equals(json.getString("ret"))){
			dataStr=jRSAUtil.publicDecrypt(json.getString("data"));
			JSONObject jsonObject = JSONObject.parseObject(dataStr);
			Integer code = jsonObject.getInteger("tkstatus"); //退款状态 1成功 0失败
			System.out.println("tkstatus:"+code);
		}
	}
	//查詢
	public static void qorder() throws IOException {
		String key="dDWkfXIw7Dak6FFMfxZkNNp77gxzpgGG";
		String url="https://shopapi.wqkeji.cn/";
		String publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA0IdduGxn1ZXZpEL0d2W9 MR7/g0fFE7elS0cabE0nVp7Mbw3jU+xAL/GlkSpicb4hXnGzLoi8asxUAsUnbuBr SAaumic0eKaiHMzeXJFr9zce4UkudxOfxqKknRJjFsXq1+vaKIKmdSpsLia1neKb ncuw2aLQS/FYbt8/2lerI5ax07t9HQ5soOz5dG2CMy68A8rqg6dIxHRpTTGa+7om k7twgPGVpFLCaS0IB2fAZ2RyIbhv/i8BKBFjSzjZQC3iGMU5IqWm8zVynkh4/M5N Nj5fedh+Q9VZtkapeQRC+7g/R2qPySX3JVTt/VzjFUHWhAVPdkykn5ruv0I3ImjF 8wIDAQAB";
		String passPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAn0mGQ1S8+yw6LA+ZLTrv 2gS0vsWxpvaDRjkI44WK8aqefeFqrPDNV7YXXKmEqiZNxd/J6TzLtVUYDOWsf/CH OL9r0xb4OdaShthU4BvKd1j+XPr9wk2gYSe/ECJU2KyxBsvSfYv2HsPbklexNoFx +iJ/vgmTtktf0yj74wa2+R2PNUeBKUW8wY422fC/hcuLIvIlANTSrb35bp7bk8ZN A8w0HLqkZCxAQRqs0ore9PoJy4P3CA7xs6UngSNFmf/9+rtbMdkCTqMjoHFg+qtw YeWvvgE8orXq+s4q19fyFXWcSypNgrnuyaG2xf8dbphOXmeh5/hBvcCCMHmFBdu3 qQIDAQAB";
		String privateKey = "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQDQh124bGfVldmk QvR3Zb0xHv+DR8UTt6VLRxpsTSdWnsxvDeNT7EAv8aWRKmJxviFecbMuiLxqzFQC xSdu4GtIBq6aJzR4pqIczN5ckWv3Nx7hSS53E5/GoqSdEmMWxerX69oogqZ1Kmwu JrWd4pudy7DZotBL8Vhu3z/aV6sjlrHTu30dDmyg7Pl0bYIzLrwDyuqDp0jEdGlN MZr7uiaTu3CA8ZWkUsJpLQgHZ8BnZHIhuG/+LwEoEWNLONlALeIYxTkipabzNXKe SHj8zk02Pl952H5D1Vm2Rql5BEL7uD9Hao/JJfclVO39XOMVQdaEBU92TKSfmu6/ QjciaMXzAgMBAAECggEBAMbaT2Uu+1WSMJmTv8ycGG5fQQnPxv+mgNnoVCoy6PCR PiIo/D9ra0pNBbYI1RfiV6/YfSZC/mpZr0N0/sWc/LESri4SdNcZED6mnuVnR63X wnrTbpve+t5Q+ulOP2/YDLsbDk7TSCky6zaXc1m8Xul69IqV2EyTOeGAw0NTlr+3 mrhF25Z6jmfntwhYmkMGFsifpYjQCB4AX2TVDxeLZQ1EG9aNSehKviqaqbj0L9+Y /0o9ExO3aK56P2oBkQDdaszvaeybe4aHun87fJ9cMekYXt/V3lo/RmLjwODW9uwm 3Zp2A5gJI72J6BqNHs2HMF63RDygkK5R2ZihlH3FpAECgYEA+PceI0hWsYQMr82T V9COdUqin9C4Byu9pLJ89NpUtqL9TnTwCF4E/+0h6G/1H0TCr1jjlR5SHvfylesl jMEiHqC8FgF7ppP0/J3BbC+8aOz66cMmWaTv+9ML16J8/C8qnoaKu1AwFiMtv+GU w9UM6izuf5LFvd+PxIGhh7H0zpECgYEA1mvAh7lcvbltNBa7Fr25uzGHRF9ZGY89 ypzZqb/DkSJA6fdjmR1Sc7+11n/coybKRO8fMEUlH6P7vzOBCJYwJ/cTh687qfta 71qe7HH/mc+ULDWgt37sDJvhaEyqTwtZjI/FGPhRhXyoRWwtmwM61Jcn+LRRf0RL GYzzeW/SVkMCgYBzBI9601+h0RSfX4Tzq9k0nM92r9FlFrMdVIxf/kHmwnPt/B4s b2s15RKmNJr04CplIda6PHDJ99itpB4L4enmZnGeZP8AHRiwYB1M4tzTSLA+sLaV 8ZohKAYvj9TYGN5CsHuFP+2HPKdVH10//7xgr3Nboq4SIydjLFQ4wWEM0QKBgQDD hsqweXV6hMZyQ3Ajkqr1anrA/HmRdy+PvWw3AOljRWsQb0lhbH2tk1pL2ZjnJg44 wh0ctVDfA3gjqOG3ZM1RBFvI1SCBJvaLYYWec23zYnjpv5Vo/tJdxR/NLmv3XJAy P0AqPvaH1V7Uv4ru2gkhpx0KC9RtS2d4SzISwMd7ZQKBgQC4XzFuJyENHQgUc4kD uKzfIPgPzXFblu9MKdxA4YdOrQeOAL68kUAkXOL5bc+Gv6roideXU/dnpuhwd5B0 NvcE/hy2bSE0RCwHkxWyK0d5CVVaLwvitb6Em6dAonH2AcLTlM2ZSEN/6GKRiTYh EQvfX5pds0zycM76KKYeO0eexw==";
		XRSAUtil xRSAUtil=new XRSAUtil(publicKey, privateKey);
		SortedMap<String, String> map = new TreeMap<String, String>();
		map.put("service","App.Order.Queryorder");//接口名(固定)
		map.put("timestamp",DateUtils.getTime());//时间格式:2022-01-01 00:00:00
		map.put("nonce", Hzxj_demo.generateNonceStr());//唯一随机字符串
		map.put("member_id","220608655");//商户ID(系统分配)
		map.put("order_id","220608655OR37D874A1D4286EEF20230104153743");//平台订单号(RSA加密,先签名再加密)
		String sign = Hzxj_demo.mapToStringAndTrimForNewSys(map)+key;
		sign = Hzxj_demo.md5encode(sign).toUpperCase();
		map.put("sign",sign);//签名(不参与签名)
		String orderIdRSA=xRSAUtil.privateEncrypt(map.get("order_id"));
		map.put("order_id",orderIdRSA);
		String callbackJson =Hzxj_demo.sendPost(url, Hzxj_demo.mapToStringAndTrim(map),"utf-8");
		//JSONObject json = JSONObject.fromObject(retMsg);
		String dataStr = "";
		JSONObject json = JSONObject.parseObject(callbackJson);
		XRSAUtil jRSAUtil=new XRSAUtil(passPublicKey, privateKey);
		if("200".equals(json.getString("ret"))){
			dataStr=jRSAUtil.publicDecrypt(json.getString("data"));
		}
		JSONObject jsonObject = JSONObject.parseObject(dataStr);
		String TRADE_SUCCESS = jsonObject.getString("status");
		System.out.println(TRADE_SUCCESS);
	}

	private static final String SYMBOLS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private static final Random RANDOM = new SecureRandom();

	/**
     * 获取随机字符串 Nonce Str
     *
     * @return String 随机字符串
     */
    public static String generateNonceStr() {
        char[] nonceChars = new char[32];
        for (int index = 0; index < nonceChars.length; ++index) {
            nonceChars[index] = SYMBOLS.charAt(RANDOM.nextInt(SYMBOLS.length()));
        }
        return new String(nonceChars);
    }

	public static String mapToStringAndTrim(SortedMap<String, String> sortedMap){
		if(sortedMap == null || sortedMap.isEmpty()){
			return "";
		}
		StringBuilder sb = new StringBuilder();
		Iterator<Map.Entry<String, String>> it = sortedMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, String> entry = it.next();
			String key = entry.getKey();
			String value = entry.getValue();
			if(value != null && !"".equals(value.trim())){
				sb.append(key.trim()).append("=").append(value.trim()).append("&");
			}
		}
		if(sb.length() > 0 && sb.charAt(sb.length()-1)=='&'){
			sb.deleteCharAt(sb.length()-1);
		}
		return sb.toString();
	}

	public static String mapToStringAndTrimForNewSys(SortedMap<String, String> sortedMap){
		if(sortedMap == null || sortedMap.isEmpty()){
			return "";
		}
		StringBuilder sb = new StringBuilder();
		Iterator<Map.Entry<String, String>> it = sortedMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, String> entry = it.next();
			String key = entry.getKey();
			String value = entry.getValue();
			if(value != null){
				sb.append(value.trim());
			}
		}
		return sb.toString();
	}

	public static String sendPost(String url, String param, String charset) throws IOException {
		HttpURLConnection conn = null;
		OutputStreamWriter out = null;
		BufferedReader read = null;
		StringBuilder result = new StringBuilder();
		try {
			URL realUrl = new URL(url);
			conn = (HttpURLConnection)realUrl.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + charset);
			conn.setConnectTimeout(5000);
			conn.setReadTimeout(5000);

			out = new OutputStreamWriter(conn.getOutputStream(), charset);
			out.write(param);
			out.flush();

			int responseCode = conn.getResponseCode();
			if (responseCode >= HttpURLConnection.HTTP_OK && responseCode < HttpURLConnection.HTTP_MULT_CHOICE) {
				read = new BufferedReader(new InputStreamReader(conn.getInputStream(), charset));
				String line;
				while ((line = read.readLine()) != null) {
					result.append(line);
				}
			} else {
				throw new IOException("Server returned non-OK status: " + responseCode);
			}
		} finally {
			if (out != null) {
				out.close();
			}
			if (read != null) {
				read.close();
			}
			if (conn != null) {
				conn.disconnect();
			}
		}
		return result.toString();
	}

    public final static String md5encode(String s) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			byte[] strTemp = s.getBytes();
			MessageDigest mdTemp = MessageDigest.getInstance("MD5");
			mdTemp.update(strTemp);
			byte[] md = mdTemp.digest();
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
