package com.ece452.watfit.data;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class DietaryLogWithEntries {
    @Embedded public DietaryLog dietaryLog;

    @Relation(
        parentColumn = "id",
        entityColumn = "dietary_log_id"
    )
    public List<DietaryLogEntry> entries;
}
