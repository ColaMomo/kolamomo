package com.kolamomo.network.nio;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class MemcachedClient {
	private NioClient nioClient;
	
	public MemcachedClient() {}
	
	public MemcachedClient(String ip, int port) {
		nioClient = new NioClient(ip, port);
	}
	
	public Object get(String content)  {
		try {
			byte[] result = nioClient.sendRequest("get " + content + "\n");
			System.out.println(new String(result));
			int pos = 0;
			for(; pos < result.length; pos++) {
				if(result[pos] == '\r' && result[pos+1] == '\n') {
					break;
				}
			}
			System.out.println(pos);
			int start = pos;
			for(; start >= 0; start--) {
				if(result[start] == ' ' ) {
					break;
				}
			}
			System.out.println(start);
			int count = 0;
			for(int i = start+1; i < pos; i++) {
				System.out.println(result[i]);
				count = count * 10 + (result[i]-'0');
			}
			System.out.println(count);
			byte[] value = new byte[count];
			for(int i = 0; i < count; i++) {
				value[i] = result[pos+2+i];
				System.out.println("= " + value[i]);
			}
//            int flag = Integer.parseInt(info[2]);
//            int length = Integer.parseInt(info[3]);
//            long casUnique = forCas ? Long.parseLong(info[4]) : 0L;
//            if(log.isDebugEnabled())
//            {
//                log.debug((new StringBuilder()).append("++++ key: ").append(key).toString());
//                log.debug((new StringBuilder()).append("++++ flags: ").append(flag).toString());
//                log.debug((new StringBuilder()).append("++++ length: ").append(length).toString());
//                log.debug((new StringBuilder()).append("++++ casUnique: ").append(casUnique).toString());
//            }invalid stream header ObjectInputStream
//            byte buf[] = sock.readBytes(length);
//            if((flag & 2) == 2)
//                try
//                {
//                    buf = QuickLZ.decompress(buf);
//                }
//                catch(Exception e)
//                {
//                    if(errorHandler != null)
//                        errorHandler.handleErrorOnGet(this, e, key);
//                    log.error((new StringBuilder()).append("++++ IOException thrown while trying to uncompress input stream for key: ").append(key).toString());
//                    log.error(e.getMessage(), e);
//                    throw new NestedIOException((new StringBuilder()).append("++++ IOException thrown while trying to uncompress input stream for key: ").append(key).toString(), e);
//                }
//            if((flag & 8) != 8)
//            {
//                if(primitiveAsString || asString)
//                {
//                    if(log.isDebugEnabled())
//                        log.debug("++++ retrieving object and stuffing into a string.");
//                    casValue = new CasValue(new String(buf, defaultEncoding), casUnique);
//                } else
//                {
//                    try
//                    {
//                        casValue = new CasValue(NativeHandler.decode(buf, flag), casUnique);
//                    }
//                    catch(Exception e)
//                    {
//                        if(errorHandler != null)
//                            errorHandler.handleErrorOnGet(this, e, key);
//                        log.error((new StringBuilder()).append("++++ Exception thrown while trying to deserialize for key: ").append(key).toString(), e);
//                        throw new NestedIOException(e);
//                    }
//                }

	        ByteArrayInputStream bis = new ByteArrayInputStream (value);       
	        ObjectInputStream ois = new ObjectInputStream (bis);
			return ois.readObject();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}     
		return null;
	}
	
	public static void main(String args[]) {
		MemcachedClient mcClient = new MemcachedClient("10.73.32.69", 3120);
		String result = (String)mcClient.get("api.limit.user.day.2835469272.14416158838");
		System.out.println(result);
	}
}
