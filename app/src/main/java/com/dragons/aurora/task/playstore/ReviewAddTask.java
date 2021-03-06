/*
 * Aurora Store
 * Copyright (C) 2018  Rahul Kumar Patel <whyorean@gmail.com>
 *
 * Yalp Store
 * Copyright (C) 2018 Sergey Yeriomin <yeriomin@gmail.com>
 *
 * Aurora Store (a fork of Yalp Store )is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * Aurora Store is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Aurora Store.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.dragons.aurora.task.playstore;

import com.dragons.aurora.model.Review;
import com.dragons.aurora.model.ReviewBuilder;
import com.dragons.aurora.playstoreapiv2.GooglePlayAPI;
import com.dragons.aurora.playstoreapiv2.ReviewResponse;

import java.io.IOException;

import timber.log.Timber;

public class ReviewAddTask extends PlayStorePayloadTask<Review> {

    private com.dragons.aurora.fragment.details.Review fragment;
    private String packageName;
    private Review review;

    public void setFragment(com.dragons.aurora.fragment.details.Review fragment) {
        this.fragment = fragment;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public void setReview(com.dragons.aurora.model.Review review) {
        this.review = review;
    }

    @Override
    protected Review getResult(GooglePlayAPI api, String... arguments) throws IOException {
        ReviewResponse response = api.addOrEditReview(
                packageName,
                review.getComment(),
                review.getTitle(),
                review.getRating()
        );
        return ReviewBuilder.build(response.getUserReview());
    }

    @Override
    protected void onPostExecute(Review review) {
        if (success()) {
            fragment.fillUserReview(review);
        } else {
            Timber.e("Error adding the review: %s", getException().getMessage());
            getException().printStackTrace();
        }
    }
}
