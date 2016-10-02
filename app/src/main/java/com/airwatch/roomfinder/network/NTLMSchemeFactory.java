package com.airwatch.roomfinder.network;

/**
 * Created by jmara on 8/16/2016.
 */
import org.apache.http.auth.AuthScheme;
import org.apache.http.impl.auth.NTLMScheme;
import org.apache.http.params.HttpParams;
import org.apache.http.auth.AuthSchemeFactory;

public class NTLMSchemeFactory implements AuthSchemeFactory{
    @Override
    public AuthScheme newInstance(HttpParams params)
    {
        return new NTLMScheme(new JCIFSEngine());
    }
}
