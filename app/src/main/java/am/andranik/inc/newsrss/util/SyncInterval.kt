package am.andranik.inc.newsrss.util

import am.andranik.inc.newsrss.R
import java.util.concurrent.TimeUnit

enum class SyncInterval(val milis: Long, val title: Int) {
    MIN1(TimeUnit.MINUTES.toMillis(1), R.string.interval_minute_not_recommended),
    MIN5(TimeUnit.MINUTES.toMillis(5), R.string.interval_5_minute_not_recommended),
    MIN15(TimeUnit.MINUTES.toMillis(15), R.string.interval_15_minutes),
    MIN30(TimeUnit.MINUTES.toMillis(30), R.string.interval_30_minutes),
    HOUR(TimeUnit.HOURS.toMillis(1), R.string.interval_hour),
    HOUR2(TimeUnit.HOURS.toMillis(2), R.string.interval_2_hours),
    HOUR4(TimeUnit.HOURS.toMillis(4), R.string.interval_4_hours),
    HOUR8(TimeUnit.HOURS.toMillis(8), R.string.interval_8_hours),
    HOUR12(TimeUnit.HOURS.toMillis(12), R.string.interval_12_hours),
    DAY(TimeUnit.DAYS.toMillis(1), R.string.interval_1_day),
}