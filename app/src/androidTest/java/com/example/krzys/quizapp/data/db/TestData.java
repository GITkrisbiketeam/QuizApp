package com.example.krzys.quizapp.data.db;

import com.example.krzys.quizapp.data.dto.common.Category;
import com.example.krzys.quizapp.data.dto.common.MainPhoto;
import com.example.krzys.quizapp.data.dto.quiz.Answer;
import com.example.krzys.quizapp.data.dto.quiz.FlagAnswer;
import com.example.krzys.quizapp.data.dto.quiz.FlagResult;
import com.example.krzys.quizapp.data.dto.quiz.Image;
import com.example.krzys.quizapp.data.dto.quiz.Question;
import com.example.krzys.quizapp.data.dto.quiz.QuizData;
import com.example.krzys.quizapp.data.dto.quiz.Rate;
import com.example.krzys.quizapp.data.dto.quizzes.QuizzesItem;
import com.example.krzys.quizapp.data.dto.quizzes.Tag;

import java.util.Arrays;
import java.util.List;

/**
 * Utility class that holds values to be used for testing.
 */
@SuppressWarnings({"SpellCheckingInspection", "WeakerAccess"})
public class TestData {

    static final Tag TAG = new Tag(5984493285893249L, "grill", "tag");
    static final Tag TAG2 = new Tag(5984468048098433L, "wesele", "tag");

    static final List<Tag> TAG_LIST = Arrays.asList(TAG, TAG2);

    static final Category CATEGORY = new Category(13L, null, "kultura i sztuka", null);
    static final Category CATEGORY2 = new Category(5796577556205761L, "IAB1", "Arts & Entertainment", "category");

    static final List<Category> CATEGORY_LIST = Arrays.asList(CATEGORY, CATEGORY2);

    static final MainPhoto MAIN_PHOTO = new MainPhoto("ASphotowed", 2123, "iStock.com",
            "First wedding dance", "https://d.wpimg.pl/1561281332-1122536739/wesele.jpg", 1412, 1738029L);

    static final List<Integer> MY_ANSWERS2 = Arrays.asList(0, 1);

    static final List<Integer> MY_ANSWERS_10 = Arrays.asList(0, 1, 0, 1, 1, 0,1,1,1,1);

    static final QuizzesItem QUIZZES_ITEM = new QuizzesItem(1L, 2, "2018-04-22T13:38:13+0000", "FirstTestQuiz",
            "KNOWLEDGE", "", CATEGORY_LIST, null, CATEGORY, null, null);
    static final QuizzesItem QUIZZES_ITEM_POLL = new QuizzesItem(111L, 10, "2018-04-23T13:38:13+0000", "SecondTestQuiz",
            "HOT_OR_NOT", "SecondTestQuiz content", CATEGORY_LIST, MAIN_PHOTO, CATEGORY, TAG_LIST, MY_ANSWERS2);
    static final QuizzesItem QUIZZES_ITEM_TEST = new QuizzesItem(111L, 10, "2018-04-23T13:38:13+0000", "SecondTestQuiz",
            "PSYCHOTEST", "SecondTestQuiz content", CATEGORY_LIST, MAIN_PHOTO, CATEGORY, TAG_LIST, MY_ANSWERS_10);

    static final List<QuizzesItem> QUIZZES_ITEM_LIST = Arrays.asList(QUIZZES_ITEM, QUIZZES_ITEM_POLL, QUIZZES_ITEM_TEST);


    static final Rate RATE = new Rate(0, 20, "Pssst nikomu nie powiemy...");
    static final Rate RATE2 = new Rate(20, 40, "Kiedy w końcu zdasz na prawo jazdy?");
    static final Rate RATE3 = new Rate(40, 60, "Może być lepiej. Wystarczy kupić atlas");
    static final Rate RATE4 = new Rate(60, 80, "Dużo lepiej od średniej krajowej");
    static final Rate RATE5 = new Rate(80, 100, "Wspaniale. Znasz chyba wszystkie miasta i powiaty Polski");

    static final List<Rate> RATE_LIST = Arrays.asList(RATE, RATE2, RATE3, RATE4, RATE5);

    static final Image IMAGE_EMPTY = new Image(null, null, null, null, null, null);
    static final Image IMAGE = new Image("interfoto_rm", String.valueOf(1428),String.valueOf(1741983L), "Forum", "https://d.wpimg.pl/62283584-1811684276/natalie-portman-leon.jpg", String.valueOf(933));

    static final FlagAnswer FLAG_ANSWER = new FlagAnswer(1, null, null, null, null, null);
    static final FlagAnswer FLAG_ANSWER2 = new FlagAnswer(null, 1, null, null, null, null);
    static final FlagAnswer FLAG_ANSWER3 = new FlagAnswer(null, null, 1, null, null, null);

    static final Answer ANSWER_EMPTY_IMAGE = new Answer(IMAGE_EMPTY, 1, "Sara", null, null, null);
    static final Answer ANSWER_IMAGE = new Answer(IMAGE, 1, "Sara", null, null, null);
    static final Answer ANSWER_CORRECT = new Answer(IMAGE, 1, "Sara", 1, null, null);
    static final Answer ANSWER_POLL = new Answer(IMAGE, 1, "Sara", 1, 100, null);
    static final Answer ANSWER_POLL2 = new Answer(IMAGE, 1, "Sara", 1, 101, null);
    static final Answer ANSWER_TEST = new Answer(IMAGE, 1, "Sara", 1, null, FLAG_ANSWER);
    static final Answer ANSWER_TEST2 = new Answer(IMAGE, 1, "Sara", 1, null, FLAG_ANSWER2);
    static final Answer ANSWER_TEST3 = new Answer(IMAGE, 1, "Sara", 1, null, FLAG_ANSWER3);

    static final List<Answer> ANSWER_LIST = Arrays.asList(ANSWER_EMPTY_IMAGE, ANSWER_IMAGE, ANSWER_CORRECT);
    static final List<Answer> ANSWER_LIST_POLL = Arrays.asList(ANSWER_POLL, ANSWER_POLL2);
    static final List<Answer> ANSWER_LIST_TEST = Arrays.asList(ANSWER_TEST, ANSWER_TEST2, ANSWER_TEST3);

    static final Question QUESTION = new Question(IMAGE_EMPTY, ANSWER_LIST, "W postać Wednesday Addams wcieliła się:", "ANSWER_TEXT", "QUESTION_TEXT", 1);
    static final Question QUESTION2 = new Question(IMAGE, ANSWER_LIST, "Kto zagrał małą Claudię w \\\"Wywiadzie z wampirem\\\"?", "ANSWER_TEXT", "QUESTION_TEXT_IMAGE", 2);
    static final Question QUESTION3 = new Question(IMAGE, ANSWER_LIST, "Jaki film przyniósł sławę Scarlett Johansson?", "ANSWER_TEXT", "QUESTION_TEXT_IMAGE", 3);

    static final List<Question> QUESTION_LIST = Arrays.asList(QUESTION, QUESTION2, QUESTION3);

    static final Question QUESTION_POLL = new Question(IMAGE_EMPTY, ANSWER_LIST_POLL, "W postać Wednesday Addams wcieliła się:", "ANSWER_TEXT", "QUESTION_TEXT", 1);
    static final Question QUESTION_POLL2 = new Question(IMAGE, ANSWER_LIST_POLL, "Kto zagrał małą Claudię w \\\"Wywiadzie z wampirem\\\"?", "ANSWER_TEXT", "QUESTION_TEXT_IMAGE", 2);

    static final List<Question> QUESTION_POLL_LIST = Arrays.asList(QUESTION_POLL, QUESTION_POLL2);

    static final Question QUESTION_TEST = new Question(IMAGE_EMPTY, ANSWER_LIST_TEST, "W postać Wednesday Addams wcieliła się:", "ANSWER_TEXT", "QUESTION_TEXT", 1);
    static final Question QUESTION_TEST2 = new Question(IMAGE, ANSWER_LIST_TEST, "Kto zagrał małą Claudię w \\\"Wywiadzie z wampirem\\\"?", "ANSWER_TEXT", "QUESTION_TEXT_IMAGE", 2);
    static final Question QUESTION_TEST3 = new Question(IMAGE, ANSWER_LIST_TEST, "Kto zagrał małą Claudię w \\\"Wywiadzie z wampirem\\\"?", "ANSWER_TEXT", "QUESTION_TEXT_IMAGE", 2);

    static final List<Question> QUESTION_TEST_LIST = Arrays.asList(QUESTION_TEST, QUESTION_TEST2, QUESTION_TEST3);

    static final FlagResult FLAG_RESULT = new FlagResult(IMAGE_EMPTY, "A", "Chandler", "Czy mógłbyś być bardziej Chandlerem?");
    static final FlagResult FLAG_RESULT2 = new FlagResult(IMAGE, "B", "Ross", "Unagi...");
    static final FlagResult FLAG_RESULT3 = new FlagResult(IMAGE, "C", "Monica", "Gdzie kucharek sześć...");

    static final List<FlagResult> FLAG_RESULT_LIST = Arrays.asList(FLAG_RESULT, FLAG_RESULT2, FLAG_RESULT3);

    static final QuizData QUIZ_DATA = new QuizData(null, RATE_LIST, QUESTION_LIST, QUIZZES_ITEM.getCreatedAt(),
            QUIZZES_ITEM.getSponsored(), QUIZZES_ITEM.getTitle(), QUIZZES_ITEM.getType(), QUIZZES_ITEM.getContent(), QUIZZES_ITEM.getButtonStart(), QUIZZES_ITEM.getShareTitle(), null, QUIZZES_ITEM.getCategories(), QUIZZES_ITEM.getId(), null,
            QUIZZES_ITEM.getMainPhoto(), QUIZZES_ITEM.getCategory(), false, 1524472456L, null, 0.70900074083088, 10988L, null, null, null, false, null, QUIZZES_ITEM.getTags());
    static final QuizData QUIZ_DATA_POLL = new QuizData(null, RATE_LIST, QUESTION_POLL_LIST, QUIZZES_ITEM_POLL.getCreatedAt(),
            QUIZZES_ITEM_POLL.getSponsored(), QUIZZES_ITEM_POLL.getTitle(), QUIZZES_ITEM_POLL.getType(), QUIZZES_ITEM_POLL.getContent(), QUIZZES_ITEM_POLL.getButtonStart(), QUIZZES_ITEM_POLL.getShareTitle(), null, QUIZZES_ITEM_POLL.getCategories(), QUIZZES_ITEM_POLL.getId(), null,
            QUIZZES_ITEM_POLL.getMainPhoto(), QUIZZES_ITEM_POLL.getCategory(), false, 1524472456L, null, 0d, 10988L, null, null, null, false, null, QUIZZES_ITEM_POLL.getTags());
    static final QuizData QUIZ_DATA_TEST = new QuizData(null, RATE_LIST, QUESTION_TEST_LIST, QUIZZES_ITEM_TEST.getCreatedAt(),
            QUIZZES_ITEM_TEST.getSponsored(), QUIZZES_ITEM_TEST.getTitle(), QUIZZES_ITEM_TEST.getType(), QUIZZES_ITEM_TEST.getContent(), QUIZZES_ITEM_TEST.getButtonStart(), QUIZZES_ITEM_TEST.getShareTitle(), FLAG_RESULT_LIST, QUIZZES_ITEM_TEST.getCategories(), QUIZZES_ITEM_TEST.getId(), null,
            QUIZZES_ITEM_TEST.getMainPhoto(), QUIZZES_ITEM_TEST.getCategory(), false, 1524472456L, null, 0.70900074083088, 10988L, null, null, null, false, null, null);

    static final List<QuizData> QUIZ_DATA_LIST = Arrays.asList(QUIZ_DATA, QUIZ_DATA_POLL, QUIZ_DATA_TEST);
}
