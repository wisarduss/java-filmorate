package ru.yandex.practicum.filmorate.model;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor(force = true)
@AllArgsConstructor
@RequiredArgsConstructor
@SuperBuilder
public class Friend {
    private Long id;
    @NonNull
    private Long userId;
    @NonNull
    private Long friendId;
    @NonNull
    private FriendStatus friendStatus;
}
