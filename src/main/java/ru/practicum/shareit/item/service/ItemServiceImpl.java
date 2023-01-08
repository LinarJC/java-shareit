package ru.practicum.shareit.item.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.dto.BookingDtoForItem;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.BookingException;
import ru.practicum.shareit.exception.StorageException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.comment.CommentMapper;
import ru.practicum.shareit.item.comment.CommentRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithBooking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    @Autowired
    public ItemServiceImpl(ItemRepository itemRepository, ItemMapper itemMapper,
                           UserRepository userRepository, BookingRepository bookingRepository,
                           BookingMapper bookingMapper, CommentRepository commentRepository,
                           CommentMapper commentMapper) {
        this.itemRepository = itemRepository;
        this.itemMapper = itemMapper;
        this.userRepository = userRepository;
        this.bookingRepository = bookingRepository;
        this.bookingMapper = bookingMapper;
        this.commentRepository = commentRepository;
        this.commentMapper = commentMapper;
    }

    @Override
    public ItemDtoWithBooking findById(Long itemId, Long userId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new StorageException("Вещь с ID " + itemId + " не найден"));
        ItemDtoWithBooking itemDtoWithBooking = itemMapper
                .toItemDtoWithBooking(item);
        if (Objects.equals(item.getOwner().getId(), userId)) {
            createItemDtoWithBooking(itemDtoWithBooking);
        }
        List<Comment> comments = commentRepository.findAllByItemId(itemId);
        itemDtoWithBooking.setComments(comments
                .stream().map(commentMapper::toCommentDto)
                .collect(Collectors.toList())
        );
        return itemDtoWithBooking;
    }

    @Override
    public List<ItemDtoWithBooking> findAll(Long userId) {
        List<ItemDtoWithBooking> result = itemRepository.findAll().stream()
                .filter(item -> item.getOwner().getId().equals(userId))
                .map(itemMapper::toItemDtoWithBooking)
                .collect(Collectors.toList());
        for (ItemDtoWithBooking itemDtoWithBooking : result) {
            createItemDtoWithBooking(itemDtoWithBooking);
            List<Comment> comments = commentRepository.findAllByItemId(itemDtoWithBooking.getId());
            if (!comments.isEmpty()) {
                itemDtoWithBooking.setComments(comments
                        .stream().map(commentMapper::toCommentDto)
                        .collect(Collectors.toList()));
            }
        }
        result.sort(Comparator.comparing(ItemDtoWithBooking::getId));
        return result;
    }

    private void createItemDtoWithBooking(ItemDtoWithBooking itemDtoWithBooking) {
        List<Booking> lastBookings = bookingRepository
                .findByItemIdAndEndIsBeforeOrderByEndDesc(itemDtoWithBooking.getId(),
                        LocalDateTime.now());
        if (!lastBookings.isEmpty()) {
            BookingDtoForItem lastBooking = bookingMapper.toBookingDtoForItem(lastBookings.get(0));
            itemDtoWithBooking.setLastBooking(lastBooking);
        }
        List<Booking> nextBookings = bookingRepository
                .findByItemIdAndStartIsAfterOrderByStartDesc(itemDtoWithBooking.getId(),
                        LocalDateTime.now());
        if (!nextBookings.isEmpty()) {
            BookingDtoForItem nextBooking = bookingMapper.toBookingDtoForItem(nextBookings.get(0));
            itemDtoWithBooking.setNextBooking(nextBooking);
        }
    }

    @Override
    public ItemDto save(ItemDto itemDto, Long userId) {
        Item item = itemMapper.toItem(itemDto);
            item.setOwner(userRepository.findById(userId).orElseThrow(() -> new StorageException("Incorrect userId")));
            return itemMapper.toItemDto(itemRepository.save(item));
    }

    @Override
    public CommentDto saveComment(Long userId, Long itemId, CommentDto commentDto) {
        Item item = itemRepository.findById(itemId).orElseThrow(() ->
                new StorageException("Вещи с Id = " + itemId + " нет в БД"));
        User user = userRepository.findById(userId).orElseThrow(() ->
                new StorageException("Пользователя с Id = " + userId + " нет в БД"));
        Booking booking = new Booking();
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStatus(Status.APPROVED);
        Example<Booking> example = Example.of(booking);
        if (bookingRepository.exists(example)) {
            throw new BookingException("Пользователь с Id = " + userId + " не брал в аренду вещь с Id = " + itemId);
        }
        Comment comment = commentMapper.toComment(commentDto);
        comment.setItem(item);
        comment.setAuthor(user);
        return commentMapper.toCommentDto(commentRepository.save(comment));
    }

    @Override
    public ItemDto update(ItemDto itemDto, Long userId, Long id) {
        try {
            Item oldItem = itemRepository.findById(id).orElseThrow();

            if (Objects.equals(oldItem.getOwner().getId(), userId)) {

                if (itemDto.getName() != null) {
                    oldItem.setName(itemDto.getName());
                }
                if (itemDto.getDescription() != null) {
                    oldItem.setDescription(itemDto.getDescription());
                }
                if (itemDto.getAvailable() != null) {
                    oldItem.setAvailable(itemDto.getAvailable());
                }
                log.info("Внесены изменения в данные вещи: '{}', ID '{}', '{}'",
                        itemDto.getName(), itemDto.getId(), itemDto.getDescription());
                return itemMapper.toItemDto(itemRepository.save(oldItem));
            } else {
                throw new StorageException("Incorrect userId");
            }
        } catch (Exception e) {
            throw new StorageException("Incorrect ItemId");
        }
    }

    @Override
    public void deleteById(Long itemId) {
        itemRepository.deleteById(itemId);
    }

    @Override
    public List<ItemDto> searchItem(String text) {
        if (!text.isBlank()) {
            return itemRepository.search(text)
                    .stream()
                    .filter(Item::getAvailable)
                    .map(itemMapper::toItemDto)
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}