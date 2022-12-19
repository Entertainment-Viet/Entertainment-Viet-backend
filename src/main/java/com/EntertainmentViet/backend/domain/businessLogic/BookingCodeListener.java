package com.EntertainmentViet.backend.domain.businessLogic;

import com.EntertainmentViet.backend.domain.entities.booking.Booking;
import lombok.experimental.UtilityClass;

import javax.persistence.PrePersist;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public class BookingCodeListener {

  private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

  @PrePersist
  public void updateUidPrePersist(Booking booking) {
    if (booking.getBookingCode() == null) {
      booking.setBookingCode(generateBookingCode(booking));
    }
  }

  /**
   * Generating bookingCode from booking detail. The final code will have 3 part: YYYYMMDD-XXABCDE where
   * - YYYYMMDD: the performance date time
   * - XX: PearsonHash from organizerUid_talentUid
   * - ABCDE: 5 last letter of each section separated by '-' in bookingUid
   * @param booking
   * @return
   */
  private String generateBookingCode(Booking booking) {
    String datePart = booking.getJobDetail().getPerformanceStartTime().format(formatter);

    var organizerUid = booking.getOrganizer().getUid();
    var talentUid = booking.getTalent().getUid();
    String accountPart = PearsonHash.hash(organizerUid.toString() + "_" + talentUid.toString());

    var bookingUid = booking.getUid();
    String[] splitArr = bookingUid.toString().split("-");
    StringBuilder builder = new StringBuilder();
    for (var sub : splitArr) {
      builder.append(sub.charAt(sub.length()-1));
    }
    String bookingPart = builder.toString();

    return String.format("%s-%s%s", datePart, accountPart, bookingPart).toUpperCase();
  }

  @UtilityClass
  private class PearsonHash {
    private final List<Integer> hashTable = Arrays.asList(
        4, 95, 97, 184, 3, 93, 149, 103, 190, 13, 66, 32, 195, 16, 101, 158, 214, 86, 109, 33, 61, 121, 170, 91, 74, 106,
        193, 185, 218, 228, 160, 73, 45, 85, 248, 117, 127, 239, 108, 43, 65, 57, 209, 132, 14, 135, 215, 232, 39, 176,
        94, 200, 136, 138, 82, 115, 20, 198, 230, 83, 19, 110, 251, 180, 203, 177, 225, 222, 244, 72, 212, 130, 11, 37,
        236, 173, 12, 35, 221, 28, 76, 70, 96, 77, 134, 150, 21, 144, 133, 254, 64, 143, 49, 99, 235, 253, 105, 241, 205,
        197, 199, 98, 89, 41, 8, 141, 154, 165, 80, 112, 172, 238, 131, 152, 147, 40, 231, 111, 38, 47, 42, 81, 36, 161,
        79, 224, 60, 204, 27, 139, 157, 1, 186, 51, 126, 58, 119, 162, 242, 183, 107, 252, 213, 255, 18, 249, 233, 9, 63,
        34, 171, 243, 178, 6, 52, 223, 128, 210, 15, 202, 29, 30, 22, 151, 46, 191, 125, 142, 247, 206, 201, 31, 189, 54,
        220, 68, 90, 118, 26, 102, 175, 155, 17, 192, 116, 84, 24, 92, 55, 25, 216, 88, 194, 226, 100, 163, 78, 59, 7,
        156, 137, 211, 69, 179, 250, 104, 124, 56, 71, 187, 44, 153, 174, 129, 196, 159, 75, 208, 148, 167, 227, 145, 146,
        240, 10, 188, 181, 114, 48, 237, 5, 87, 113, 234, 0, 2, 229, 53, 164, 182, 219, 245, 140, 23, 122, 62, 67, 50,
        166, 168, 246, 217, 123, 120, 169, 207);

    public String hash(String text) {
      int hash = text.length() % 256;
      for (int i = 0; i < text.length(); i++) {
        hash = hashTable.get(hash ^ (int) text.charAt(i));
      }
      return String.valueOf(hash);
    }
  }
}
