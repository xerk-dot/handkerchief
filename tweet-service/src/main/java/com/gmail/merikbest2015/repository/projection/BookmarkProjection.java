package com.gmail.merikbest2015.repository.projection;

import java.time.LocalDateTime;

public interface BookmarkProjection {
    LocalDateTime getBookmarkDate();
    TweetProjection getTweet();
}
