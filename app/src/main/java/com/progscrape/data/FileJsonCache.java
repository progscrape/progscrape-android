package com.progscrape.data;

import android.app.Application;

import com.grack.nanojson.JsonObject;
import com.grack.nanojson.JsonParser;
import com.grack.nanojson.JsonParserException;
import com.grack.nanojson.JsonWriter;
import com.octo.android.robospice.persistence.exception.CacheCreationException;
import com.octo.android.robospice.persistence.exception.CacheLoadingException;
import com.octo.android.robospice.persistence.exception.CacheSavingException;
import com.octo.android.robospice.persistence.file.InFileObjectPersister;
import com.octo.android.robospice.persistence.keysanitation.DefaultKeySanitizer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileJsonCache extends InFileObjectPersister<JsonObject> {
    public FileJsonCache(Application application, Class<JsonObject> clazz, File cacheFolder) throws CacheCreationException {
        super(application, clazz, cacheFolder);
        setKeySanitizer(new DefaultKeySanitizer());
    }

    @Override
    protected JsonObject readCacheDataFromFile(File file) throws CacheLoadingException {
        try (FileInputStream in = new FileInputStream(file)) {
            return JsonParser.object().from(in);
        } catch (IOException e) {
            throw new CacheLoadingException(e);
        } catch (JsonParserException e) {
            throw new CacheLoadingException(e);
        }
    }

    @Override
    public JsonObject saveDataToCacheAndReturnData(JsonObject data, Object cacheKey) throws CacheSavingException {
        try (FileOutputStream out = new FileOutputStream(getCacheFile(cacheKey))) {
            JsonWriter.on(out).object(data).done();
        } catch (FileNotFoundException e) {
            throw new CacheSavingException(e);
        } catch (IOException e) {
            throw new CacheSavingException(e);
        }

        return data;
    }
}
