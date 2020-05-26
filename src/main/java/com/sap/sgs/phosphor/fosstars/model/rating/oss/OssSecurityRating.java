package com.sap.sgs.phosphor.fosstars.model.rating.oss;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sap.sgs.phosphor.fosstars.model.Label;
import com.sap.sgs.phosphor.fosstars.model.Score;
import com.sap.sgs.phosphor.fosstars.model.rating.AbstractRating;
import com.sap.sgs.phosphor.fosstars.model.score.oss.OssSecurityScore;
import java.util.Objects;

/**
 * This is a security rating for open-source projects
 * that is based on a security score for open-source project.
 */
public class OssSecurityRating extends AbstractRating {

  /**
   * A set of labels for the rating.
   */
  public enum SecurityLabel implements Label {

    BAD, MODERATE, GOOD
  }

  /**
   * Thresholds for labels.
   */
  private final Thresholds thresholds;

  /**
   * Initializes a security rating with defaults.
   */
  public OssSecurityRating() {
    this(new OssSecurityScore(), Thresholds.DEFAULT);
  }

  /**
   * Initializes a security rating based on a security score for open-source projects.
   *
   * @param score The security score.
   * @param thresholds Thresholds for labels.
   */
  @JsonCreator
  public OssSecurityRating(
      @JsonProperty("score") OssSecurityScore score,
      @JsonProperty("thresholds") Thresholds thresholds) {

    super("Security rating for open-source projects", score);
    Objects.requireNonNull(thresholds, "Oh no! Thresholds is null!");
    this.thresholds = thresholds;
  }

  @Override
  public OssSecurityScore score() {
    return (OssSecurityScore) super.score();
  }

  /**
   * Implements a mapping from a score to a label.
   */
  @Override
  protected SecurityLabel label(double score) {
    Score.check(score);

    if (score < thresholds.moderate) {
      return SecurityLabel.BAD;
    }

    if (score < thresholds.good) {
      return SecurityLabel.MODERATE;
    }

    return SecurityLabel.GOOD;
  }

  /**
   * Holds thresholds for labels.
   */
  public static class Thresholds {

    /**
     * The default thresholds.
     */
    public static final Thresholds DEFAULT = new Thresholds(5.0, 8.0);

    /**
     * A threshold for the moderate label.
     */
    private final double moderate;

    /**
     * A threshold for the good label.
     */
    private final double good;

    /**
     * Initialize thresholds.
     *
     * @param moderate A threshold for the moderate label.
     * @param good A threshold for the good label.
     */
    @JsonCreator
    public Thresholds(
        @JsonProperty("moderate") double moderate, @JsonProperty("good") double good) {

      Score.check(moderate);
      Score.check(good);

      if (moderate >= good) {
        throw new IllegalArgumentException(
            "On ho! The moderate threshold is greater or equal to the good one!");
      }

      this.moderate = moderate;
      this.good = good;
    }
  }

}
