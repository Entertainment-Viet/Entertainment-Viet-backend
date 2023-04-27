package com.EntertainmentViet.backend.domain.businessLogic;

import com.EntertainmentViet.backend.domain.entities.talent.PriorityScore;
import com.EntertainmentViet.backend.domain.entities.talent.Talent;
import com.EntertainmentViet.backend.exception.rest.InconsistentEntityStateException;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@UtilityClass
@Slf4j
public class ScoreSystemLogic {

  public void updateScoreSystemOfTalent(List<PriorityScore> newScores, Talent talent, boolean isAdmin) {
    // Setup existing Score storage
    Map<String, PriorityScore> scoreAchievementToExistSongs = new HashMap<>();
    Map<Long, PriorityScore> scoreTypeIdToExistRewards = new HashMap<>();
    for (PriorityScore curScore : talent.getPriorityScores()) {
      if (curScore.checkIfSongScore()) {
        scoreAchievementToExistSongs.put(curScore.getAchievement(), curScore);
      } else {
        scoreTypeIdToExistRewards.put(curScore.getScoreType().getId(), curScore);
      }
    }

    List<String> finalSongId = new ArrayList<>();
    List<Long> finalRewardScoreTypeId = new ArrayList<>();
    newScores.forEach(score -> {
      // Update Song List
      if (score.checkIfSongScore()) {
        var existSong = scoreAchievementToExistSongs.get(score.getAchievement());
        // Song didn't exist yet
        if (existSong == null) {
          var newSongScore = PriorityScore.builder()
              .talent(talent)
              .scoreType(score.getScoreType())
              .proof(score.getProof())
              .achievement(score.getAchievement())
              .approved(isAdmin ? score.getApproved() : false)
              .build();

          // Add new Song to current Score List to check if there is duplicated Song, it will override the existing one.
          scoreAchievementToExistSongs.put(score.getAchievement(), newSongScore);
          // Add new Song to the final Score list
          finalSongId.add(score.getAchievement());
          talent.addScore(newSongScore);
        }
        // If Song already exist
        else {
          existSong.setAchievement(score.getAchievement());
          existSong.setProof(score.getProof());
          existSong.setApproved(isAdmin ? score.getApproved() : false);
          finalSongId.add(score.getAchievement());
        }
      }
      // Update for Reward List
      else {
        if (score.getScoreType() == null) {
          throw new InconsistentEntityStateException(String.format("Can not find score type for achievement %s of talent with uid '%s'", score.getAchievement(), talent.getUid()));
        }
        var existReward = scoreTypeIdToExistRewards.get(score.getScoreType().getId());
        // If Reward didn't exist yet
        if (existReward == null) {
          var newRewardScore = PriorityScore.builder()
              .talent(talent)
              .scoreType(score.getScoreType())
              .proof(score.getProof())
              .achievement(score.getAchievement())
              .approved(isAdmin ? score.getApproved() : false)
              .build();

          // Add new Reward to current Score List to check if there is duplicated Reward, it will override the existing one.
          scoreTypeIdToExistRewards.put(score.getScoreType().getId(), newRewardScore);
          // Add new Reward to the current Score list
          finalRewardScoreTypeId.add(score.getScoreType().getId());
          talent.addScore(newRewardScore);
        }
        // If Reward already exist
        else {
          existReward.setAchievement(score.getAchievement());
          existReward.setProof(score.getProof());
          existReward.setApproved(isAdmin ? score.getApproved() : false);
          finalRewardScoreTypeId.add(score.getScoreType().getId());
        }
      }

    });
    // Remove unspecified Score
    scoreAchievementToExistSongs.entrySet().stream()
        .filter(entry -> !finalSongId.contains(entry.getKey()))
        .forEach(entry -> talent.removeScore(entry.getValue()));

    scoreTypeIdToExistRewards.entrySet().stream()
        .filter(entry -> !finalRewardScoreTypeId.contains(entry.getKey()))
        .forEach(entry -> talent.removeScore(entry.getValue()));
  }
}
