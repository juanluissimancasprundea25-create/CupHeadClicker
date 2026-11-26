package com.juanluis.cupheadclicker.data;

import static org.junit.Assert.assertEquals;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.juanluis.cupheadclicker.ui.data.db.AppDatabase;
import com.juanluis.cupheadclicker.ui.data.db.PlayerDao;
import com.juanluis.cupheadclicker.ui.data.db.PlayerEntity;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

/**
 * Instrumented tests of {@link PlayerDao} with an in-memory database.
 * They run on an emulator or a real device.
 *
 * @author Juan Luis Simancas Prundea
 * @version 1.0
 */
@RunWith(AndroidJUnit4.class)
public class PlayerDaoTest {

    private AppDatabase database;
    private PlayerDao dao;

    @Before
    public void createDatabase() {
        Context context = ApplicationProvider.getApplicationContext();
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
        dao = database.playerDao();
    }

    @After
    public void closeDatabase() {
        database.close();
    }

    @Test
    public void insertIfAbsent_doesNotOverwriteProgress() {
        PlayerEntity player = new PlayerEntity("Juan");
        player.score = 500;
        dao.upsert(player);

        dao.insertIfAbsent(new PlayerEntity("Juan"));

        assertEquals(500, dao.findByUsername("Juan").score);
    }

    @Test
    public void getAllByScore_returnsBestFirst() {
        PlayerEntity low = new PlayerEntity("Low");
        low.score = 10;
        PlayerEntity high = new PlayerEntity("High");
        high.score = 900;
        dao.upsert(low);
        dao.upsert(high);

        List<PlayerEntity> ranking = dao.getAllByScore();

        assertEquals("High", ranking.get(0).username);
        assertEquals(2, ranking.size());
    }
}
