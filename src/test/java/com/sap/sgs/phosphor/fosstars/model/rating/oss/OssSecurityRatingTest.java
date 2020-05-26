package com.sap.sgs.phosphor.fosstars.model.rating.oss;

import static com.sap.sgs.phosphor.fosstars.model.rating.oss.OssSecurityRating.SecurityLabel.BAD;
import static com.sap.sgs.phosphor.fosstars.model.rating.oss.OssSecurityRating.SecurityLabel.GOOD;
import static com.sap.sgs.phosphor.fosstars.model.rating.oss.OssSecurityRating.SecurityLabel.MODERATE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import com.sap.sgs.phosphor.fosstars.model.Parameter;
import com.sap.sgs.phosphor.fosstars.model.Rating;
import com.sap.sgs.phosphor.fosstars.model.RatingRepository;
import com.sap.sgs.phosphor.fosstars.model.Score;
import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.other.ImmutabilityChecker;
import com.sap.sgs.phosphor.fosstars.model.other.MakeImmutable;
import com.sap.sgs.phosphor.fosstars.model.other.Utils;
import com.sap.sgs.phosphor.fosstars.model.rating.oss.OssSecurityRating.Thresholds;
import com.sap.sgs.phosphor.fosstars.model.score.oss.OssSecurityScore;
import com.sap.sgs.phosphor.fosstars.model.value.RatingValue;
import java.util.Set;
import org.junit.Test;

public class OssSecurityRatingTest {

  @Test
  public void testCalculate() {
    Rating rating = RatingRepository.INSTANCE.rating(OssSecurityRating.class);
    Set<Value> values = Utils.allUnknown(rating.allFeatures());
    RatingValue ratingValue = rating.calculate(values);
    assertTrue(Score.INTERVAL.contains(ratingValue.score()));
    assertEquals(BAD, ratingValue.label());
  }

  @Test
  public void testEqualsAndHashCode() {
    OssSecurityRating one = new OssSecurityRating(new OssSecurityScore(), Thresholds.DEFAULT);
    OssSecurityRating two = new OssSecurityRating(new OssSecurityScore(), Thresholds.DEFAULT);

    assertEquals(one, two);
    assertEquals(one.hashCode(), two.hashCode());
  }

  @Test
  public void testMakeImmutableWithVisitor() {
    OssSecurityRating r = new OssSecurityRating(new OssSecurityScore(), Thresholds.DEFAULT);

    // first, check that the underlying score is mutable
    assertFalse(r.score().isImmutable());
    for (Parameter parameter : r.score().parameters()) {
      assertFalse(parameter.isImmutable());
    }
    ImmutabilityChecker checker = new ImmutabilityChecker();
    r.accept(checker);
    assertFalse(checker.allImmutable());

    // next, make it immutable
    r.accept(new MakeImmutable());

    // then, check that the underlying score became immutable
    assertTrue(r.score().isImmutable());
    for (Parameter parameter : r.score().parameters()) {
      assertTrue(parameter.isImmutable());
    }
    checker = new ImmutabilityChecker();
    r.accept(checker);
    assertTrue(checker.allImmutable());
  }

  @Test
  public void testLabels() {
    OssSecurityScore score = mock(OssSecurityScore.class);
    OssSecurityRating rating = new OssSecurityRating(score, new Thresholds(1.0, 9.0));
    assertEquals(BAD, rating.label(0.5));
    assertEquals(MODERATE, rating.label(1.5));
    assertEquals(GOOD, rating.label(9.5));
  }
}