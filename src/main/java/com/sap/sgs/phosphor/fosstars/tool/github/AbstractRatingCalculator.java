package com.sap.sgs.phosphor.fosstars.tool.github;

import com.sap.sgs.phosphor.fosstars.data.NoUserCallback;
import com.sap.sgs.phosphor.fosstars.data.UserCallback;
import java.io.IOException;
import java.util.Objects;
import org.kohsuke.github.GitHub;

/**
 * A base class for rating calculators.
 */
abstract class AbstractRatingCalculator {

  /**
   * An interface to GitHub.
   */
  final GitHub github;

  /**
   * A token for accessing the GitHub APIs.
   */
  String token;

  /**
   * An interface for interacting with a user.
   */
  UserCallback callback = NoUserCallback.INSTANCE;

  /**
   * Initializes a new calculator.
   *
   * @param github An interface to GitHub.
   */
  AbstractRatingCalculator(GitHub github) {
    this.github = Objects.requireNonNull(github, "Oh no! An interface to GitHub can't be null!");
  }

  /**
   * Sets a token for accessing the GitHub APIs.
   *
   * @param token The token.
   * @return The same calculator.
   */
  AbstractRatingCalculator token(String token) {
    this.token = token;
    return this;
  }

  /**
   * Sets an interface for interacting with a user.
   *
   * @param callback The interface for interacting with a user.
   * @return The same calculator.
   */
  AbstractRatingCalculator set(UserCallback callback) {
    this.callback = callback;
    return this;
  }

  /**
   * Calculates a rating for a specified project.
   *
   * @param project The project.
   * @return The same calculator.
   * @throws IOException If something went wrong.
   */
  abstract AbstractRatingCalculator calculateFor(GitHubProject project) throws IOException;
}
