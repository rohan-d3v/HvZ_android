package edu.acase.hvz.hvz_app;

import edu.acase.hvz.hvz_app.api.models.ZombieReportModel;
import edu.acase.hvz.hvz_app.api.requests.ZombieReportRequest;
import edu.acase.hvz.hvz_app.reports.CreateZombieReportActivity;
import edu.acase.hvz.hvz_app.reports.EditZombieReportActivity;

/** The activity for human players.
 * @see BasePlayerActivity the abstract base for more information */

public class HumanActivity extends BasePlayerActivity<ZombieReportModel> {
    static final Logger logger = new Logger("human_activity");
    static final ZombieReportRequest zombieReportRequest = new ZombieReportRequest();

    public HumanActivity() {
        super(logger, zombieReportRequest);
    }

    @Override
    int getContentView() {
        return R.layout.activity_human;
    }

    @Override
    Class<?> getEditReportIntentClass() {
        return EditZombieReportActivity.class;
    }

    @Override
    Class<?> getCaughtButtonIntentClass() {
        return IncubatingActivity.class;
    }

    @Override
    Class<?> getCreateReportIntentClass() {
        return CreateZombieReportActivity.class;
    }
}
