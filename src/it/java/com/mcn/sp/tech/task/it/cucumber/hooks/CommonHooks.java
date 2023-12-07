package com.mcn.sp.tech.task.it.cucumber.hooks;

import com.mcn.sp.tech.task.it.utils.DatabaseUtils;
import io.cucumber.java.Before;
import org.springframework.beans.factory.annotation.Autowired;

public class CommonHooks {
    @Autowired
    private DatabaseUtils databaseUtils;

    @Before
    public void clearDatabase() {
        databaseUtils.clearMeterReadings();
    }

}
