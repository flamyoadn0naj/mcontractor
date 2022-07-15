-- phpMyAdmin SQL Dump
-- version 5.0.2
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1:3306
-- Generation Time: Mar 19, 2021 at 01:49 AM
-- Server version: 8.0.20
-- PHP Version: 7.4.7

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `mypls`
--

-- --------------------------------------------------------

--
-- Table structure for table `courses`
--

CREATE TABLE `courses` (
  `id` int NOT NULL,
  `course_name` varchar(255) DEFAULT NULL,
  `meeting_days` varchar(100) DEFAULT NULL,
  `start_time` varchar(10) DEFAULT NULL,
  `end_time` varchar(10) DEFAULT NULL,
  `start_date` varchar(10) DEFAULT NULL,
  `end_date` varchar(10) DEFAULT NULL,
  `credits` int DEFAULT NULL,
  `total_capacity` int DEFAULT NULL,
  `enrolled` int DEFAULT NULL,
  `status` varchar(50) DEFAULT NULL,
  `obj` varchar(4369) DEFAULT NULL,
  `profId` int DEFAULT NULL,
  `prereq` int DEFAULT NULL,
  `requirements` varchar(10000) DEFAULT ''
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `courses`
--

INSERT INTO `courses` (`id`, `course_name`, `meeting_days`, `start_time`, `end_time`, `start_date`, `end_date`, `credits`, `total_capacity`, `enrolled`, `status`, `obj`, `profId`, `prereq`, `requirements`) VALUES
(1, 'Principles of Accounting', 'Monday, Wednesday, Friday', '12:00', '13:15', '2020-08-01', '2020-12-15', 3, 30, 0, 'Current', 'Learning accounting', 141, NULL, ''),
(21, 'Teaching Skills Workshop', 'Friday', '10:00', '11:10', '2020-10-20', '2020-12-30', 2, 20, 0, 'Current', 'At t the end of this course, you will be able to: (1) explain (in written) factors that impact the teaching and learning process (2) write and critique objectives that contain performance, conditions, and criteria', 161, NULL, ''),
(31, 'Foundations of Software Engineering', 'Tuesday, Wednesday, Thursday', '09:15', '10:45', '2020-08-24', '2020-12-15', 3, 25, 0, 'Current', 'At the end of the course, you will learn all about the basics of software engineering ', 131, NULL, ''),
(61, 'Principles of Economics', 'Tuesday, Wednesday, Thursday', '10:23', '11:24', '2020-10-23', '2020-10-25', 3, 5, 0, 'Completed', 'At t the end of this course, you will be able to: (1) explain (in written) factors that impact the teaching and learning process (2) write and critique objectives that contain performance, conditions, and criteria', 151, NULL, ''),
(131, 'Teaching Skills Workshop', 'Monday, Wednesday, Friday', '10:00', '11:10', '2020-10-20', '2020-12-30', 5, 25, 0, 'Current', 'At t the end of this course, you will be able to: (1) explain (in written) factors that impact the teaching and learning process (2) write and critique objectives that contain performance, conditions, and criteria', 131, NULL, ''),
(151, 'Theory of Programming', 'Monday, Wednesday', '12:27', '14:27', '2020-10-30', '2020-11-20', 3, 23, 0, 'Completed', 'At t the end of this course, you will be able to: (1) explain (in written) factors that impact the teaching and learning process (2) write and critique objectives that contain performance, conditions, and criteria', 151, NULL, 'Students must get atleast a C to pass.'),
(161, 'Principles of Economics', 'Tuesday, Wednesday, Thursday', '12:30', '13:30', '2020-10-20', '2020-10-31', 3, 20, 0, 'Completed', '1. This is annoying', 141, NULL, ''),
(221, 'Introduction to Machine Learning', 'Tuesday, Thursday, Friday', '09:01', '11:01', '2020-10-28', '2020-11-07', 3, 5, 0, 'Completed', 'At t the end of this course, you will be able to: (1) explain (in written) factors that impact the teaching and learning process (2) write and critique objectives that contain performance, conditions, and criteria', 161, NULL, ''),
(251, 'Introduction to Machine Learning', 'Monday, Wednesday, Friday', '12:29', '13:29', '2020-11-06', '2020-11-27', 3, 6, 0, 'Current', 'Introduction to Machine Learning', 151, NULL, 'Must get atleast a C to pass'),
(291, 'Intermediate Topics in Accounting', 'Monday, Wednesday, Friday', '13:10', '14:00', '2021-01-13', '2021-05-13', 3, 5, 0, 'Upcoming', 'Intermediate Topics in Accounting', 141, 1, ''),
(301, 'SWEN-610', 'Monday, Wednesday', '10:30', '12:30', '2020-12-01', '2020-12-31', 3, 20, 0, 'Upcoming', 'SWEN-610', 151, NULL, ''),
(1069, 'Software Theory', 'Monday', '10:56', '14:53', '2020-11-27', '2020-11-28', 1, 1, 0, 'Upcoming', 'Nothing', 161, NULL, '');

-- --------------------------------------------------------

--
-- Table structure for table `course_ratings`
--

CREATE TABLE `course_ratings` (
  `course_id` int NOT NULL,
  `score` int NOT NULL,
  `feedback` varchar(1250) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `course_ratings`
--

INSERT INTO `course_ratings` (`course_id`, `score`, `feedback`) VALUES
(21, 5, 'Good interactive class'),
(21, 4, 'Class did not take up too much time. I had fun.'),
(151, 3, 'Too much work!'),
(61, 2, 'A little dry on the concepts but the instructor tried to make it fun.'),
(291, 4, 'Course has been interesting so far.'),
(291, 1, 'Too much work!'),
(291, 4, 'Keeps you engaged'),
(151, 4, 'Course has been interesting so far.'),
(151, 4, 'Course has been interesting so far.'),
(151, 4, 'Course has been interesting so far.'),
(151, 5, 'Super interesting!');

-- --------------------------------------------------------

--
-- Table structure for table `dg_members`
--

CREATE TABLE `dg_members` (
  `user_id` int NOT NULL,
  `dg_id` int NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `dg_members`
--

INSERT INTO `dg_members` (`user_id`, `dg_id`) VALUES
(141, 21),
(161, 31),
(131, 41),
(151, 51),
(131, 61),
(151, 71),
(141, 81),
(161, 11),
(151, 101),
(141, 161),
(111, 241),
(191, 141),
(191, 211),
(11, 141),
(11, 211),
(181, 141),
(151, 271),
(151, 281),
(221, 301),
(221, 211),
(221, 281),
(221, 141),
(111, 281),
(111, 141),
(171, 211),
(11, 311),
(111, 311),
(131, 311),
(141, 311),
(151, 311),
(161, 311),
(171, 311),
(181, 311),
(191, 311),
(201, 311),
(211, 311),
(221, 311),
(241, 311),
(141, 301),
(111, 301),
(271, 311),
(111, 321),
(11, 71),
(151, 241),
(151, 341),
(181, 71),
(191, 71),
(151, 141),
(141, 231),
(141, 341),
(141, 211),
(151, 21),
(181, 21),
(191, 21),
(141, 359),
(273, 311),
(161, 360);

-- --------------------------------------------------------

--
-- Table structure for table `discussion_groups`
--

CREATE TABLE `discussion_groups` (
  `id` int NOT NULL,
  `name` varchar(250) NOT NULL,
  `course_id` int DEFAULT '0',
  `privacy` int NOT NULL DEFAULT '1'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `discussion_groups`
--

INSERT INTO `discussion_groups` (`id`, `name`, `course_id`, `privacy`) VALUES
(11, 'Introduction to Machine Learning', 221, 1),
(21, 'Principles of Accounting', 1, 1),
(31, 'Teaching Skills Workshop', 21, 1),
(41, 'Foundations of Software Engineering', 31, 1),
(51, 'Principles of Economics', 61, 1),
(61, 'Teaching Skills Workshop', 131, 1),
(71, 'Theory of Programming', 151, 1),
(81, 'Principles of Economics', 161, 1),
(101, 'Introduction to Machine Learning', 251, 1),
(141, 'Football', NULL, 0),
(161, 'Intermediate Topics in Accounting', 291, 1),
(211, 'Swimming', NULL, 0),
(231, 'Chess', NULL, 1),
(241, 'CSCI PhD Students', NULL, 0),
(271, 'SWEN-610', 301, 1),
(281, 'Golf', NULL, 0),
(301, 'Cool Learners', NULL, 1),
(311, 'My Community Channel - MCC', NULL, 0),
(321, 'Guitar Lovers', NULL, 1),
(341, 'Coding', NULL, 0),
(359, 'Demo', NULL, 1),
(360, 'Software Theory', 1069, 1);

-- --------------------------------------------------------

--
-- Table structure for table `discussion_group_content`
--

CREATE TABLE `discussion_group_content` (
  `post_id` int NOT NULL,
  `group_id` int DEFAULT NULL,
  `user_id` int DEFAULT NULL,
  `post_time` datetime DEFAULT NULL,
  `post_attachment` varchar(500) DEFAULT NULL,
  `post_name` varchar(500) DEFAULT NULL,
  `post_content` varchar(500) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `discussion_group_content`
--

INSERT INTO `discussion_group_content` (`post_id`, `group_id`, `user_id`, `post_time`, `post_attachment`, `post_name`, `post_content`) VALUES
(51, 21, 141, '2020-11-10 09:18:20', '/course/learnMat/1', 'Lesson #1', 'Check out this lesson!'),
(61, 301, 141, '2020-11-10 09:20:16', NULL, 'Welcome to Cool Learners', 'We are all learns who are cool'),
(71, 71, 151, '2020-11-10 09:52:21', NULL, 'Theory of Programming', 'bhalh'),
(81, 21, 141, '2020-11-10 09:55:16', '/course/learnMat/1', 'Lesson #2', 'Check out this lesson!'),
(84, 311, 171, '2020-11-15 16:32:06', NULL, 'Hi guys!', 'Hi everyone! I\'m so excited to start learning here!'),
(85, 311, 141, '2020-11-15 16:36:15', NULL, 'Welcome!', 'Welcome Maclcom to MyPLS'),
(89, 21, 141, '2020-11-18 11:25:36', '/course/learnMat/1', 'Lesson #1', 'Check out this lesson!'),
(90, 231, 141, '2020-11-18 11:26:39', NULL, 'Chess!', 'Demo');

-- --------------------------------------------------------

--
-- Table structure for table `discussion_group_requests`
--

CREATE TABLE `discussion_group_requests` (
  `user_id` int DEFAULT NULL,
  `dg_id` int DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `discussion_group_requests`
--

INSERT INTO `discussion_group_requests` (`user_id`, `dg_id`) VALUES
(151, 301);

-- --------------------------------------------------------

--
-- Table structure for table `enrollments`
--

CREATE TABLE `enrollments` (
  `Id` int NOT NULL,
  `userId` int DEFAULT NULL,
  `courseId` int DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `enrollments`
--

INSERT INTO `enrollments` (`Id`, `userId`, `courseId`) VALUES
(1, 151, 291),
(131, 141, 1),
(141, 161, 21),
(151, 131, 31),
(161, 151, 61),
(171, 131, 131),
(181, 151, 151),
(191, 141, 161),
(201, 161, 221),
(211, 151, 251),
(221, 141, 291),
(231, 151, 301),
(271, 181, 151),
(281, 191, 151),
(311, 11, 151),
(333, 151, 1),
(334, 181, 1),
(335, 191, 1),
(342, 161, 1069);

-- --------------------------------------------------------

--
-- Table structure for table `grades`
--

CREATE TABLE `grades` (
  `userId` int DEFAULT NULL,
  `courseId` int DEFAULT NULL,
  `lessonId` int DEFAULT NULL,
  `quizId` int DEFAULT NULL,
  `score` float DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `grades`
--

INSERT INTO `grades` (`userId`, `courseId`, `lessonId`, `quizId`, `score`) VALUES
(181, 1, 114, 93, 40),
(181, 1, 115, 94, 10),
(191, 1, 115, 94, 30),
(191, 1, 114, 93, 25);

-- --------------------------------------------------------

--
-- Table structure for table `learning_materials`
--

CREATE TABLE `learning_materials` (
  `lessonId` int DEFAULT NULL,
  `content` varchar(1250) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `learning_materials`
--

INSERT INTO `learning_materials` (`lessonId`, `content`) VALUES
(115, 'Accounts, Debits, and Credits.pdf'),
(114, 'Chapter 1.pdf'),
(114, 'mp4ExampleVid.mp4'),
(114, 'The Accounting Profession and Careers .pdf'),
(114, 'PDFTest (9).pdf');

-- --------------------------------------------------------

--
-- Table structure for table `lessons`
--

CREATE TABLE `lessons` (
  `Id` int NOT NULL,
  `courseId` int DEFAULT NULL,
  `name` varchar(50) DEFAULT NULL,
  `requirements` varchar(1250) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `lessons`
--

INSERT INTO `lessons` (`Id`, `courseId`, `name`, `requirements`) VALUES
(114, 1, 'Lesson #1', 'Must read through all materials twice'),
(115, 1, 'Lesson #2', 'Lesson Requirements'),
(119, 251, 'Chapter 1', 'Lesson Requirements'),
(120, 1069, 'New Lesson', 'Lesson Requirements');

-- --------------------------------------------------------

--
-- Table structure for table `prof_requests`
--

CREATE TABLE `prof_requests` (
  `id` int NOT NULL DEFAULT '0',
  `name` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `question_grades`
--

CREATE TABLE `question_grades` (
  `attemptId` int NOT NULL,
  `quizId` int DEFAULT NULL,
  `learnerId` int DEFAULT NULL,
  `questionId` int DEFAULT NULL,
  `response` varchar(1) DEFAULT NULL,
  `score` int DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `question_grades`
--

INSERT INTO `question_grades` (`attemptId`, `quizId`, `learnerId`, `questionId`, `response`, `score`) VALUES
(54, 93, 181, 3, 'A', 0),
(55, 93, 181, 4, 'D', 0),
(56, 93, 181, 5, 'C', 0),
(57, 93, 181, 6, 'D', 20),
(58, 93, 181, 7, 'B', 20),
(59, 94, 181, 9, 'C', 0),
(60, 94, 181, 8, 'D', 0),
(61, 94, 181, 10, 'B', 0),
(62, 94, 181, 11, 'B', 0),
(63, 94, 181, 12, 'D', 0),
(64, 94, 181, 13, 'D', 0),
(65, 94, 181, 14, 'B', 10),
(66, 94, 181, 15, 'A', 0),
(67, 94, 191, 8, 'C', 10),
(68, 94, 191, 9, 'D', 0),
(69, 94, 191, 10, 'B', 0),
(70, 94, 191, 11, 'A', 0),
(71, 94, 191, 12, 'C', 10),
(72, 94, 191, 13, 'B', 0),
(73, 94, 191, 14, 'A', 0),
(74, 94, 191, 15, 'D', 10),
(85, 93, 191, 3, 'C', 25);

-- --------------------------------------------------------

--
-- Table structure for table `quizzes`
--

CREATE TABLE `quizzes` (
  `Id` int NOT NULL,
  `lessonId` int DEFAULT NULL,
  `name` varchar(200) DEFAULT NULL,
  `completed` int DEFAULT '0',
  `totalMarks` int DEFAULT NULL,
  `minimumMarks` int DEFAULT NULL,
  `enabled` int DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `quizzes`
--

INSERT INTO `quizzes` (`Id`, `lessonId`, `name`, `completed`, `totalMarks`, `minimumMarks`, `enabled`) VALUES
(93, 114, 'Quiz 1', 0, 105, 50, 1),
(94, 115, 'Quiz #2', 0, 80, 50, 1),
(101, 115, 'Survey', 0, 12, 1, 1);

-- --------------------------------------------------------

--
-- Table structure for table `quiz_questions`
--

CREATE TABLE `quiz_questions` (
  `quizId` int NOT NULL,
  `questionID` int NOT NULL,
  `question` varchar(500) DEFAULT NULL,
  `answer` varchar(2) DEFAULT NULL,
  `mark` int DEFAULT NULL,
  `responseA` varchar(200) DEFAULT NULL,
  `responseB` varchar(200) DEFAULT NULL,
  `responseC` varchar(200) DEFAULT NULL,
  `responseD` varchar(200) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `quiz_questions`
--

INSERT INTO `quiz_questions` (`quizId`, `questionID`, `question`, `answer`, `mark`, `responseA`, `responseB`, `responseC`, `responseD`) VALUES
(93, 3, 'The accounting profession can be divided into three major categories; specifically, the practice of public accounting, private accounting, and governmental accounting. A somewhat unique and important service of public accountants is:', 'C', 25, 'Financial accounting.', 'Managerial accounting.', ' Auditing.', ' Cost accounting.'),
(93, 4, 'The primary private sector agency that oversees external financial reporting standards is the:', 'A', 20, ' Financial Accounting Standards Board.', ' Federal Bureau of Investigation.', ' General Accounting Office.', 'Internal Revenue Service.'),
(93, 5, 'Which of the following equations properly represents a derivation of the fundamental accounting equation?', 'D', 20, 'Assets + liabilities = owner\'s equity.', ' Assets = owner\'s equity.', 'Cash = assets.', 'Assets – liabilities = owner\'s equity.'),
(93, 6, 'Wilson Company owns land that cost $100,000. If a “quick sale” of the land was necessary to generate cash, the company feels it would receive only $80,000. The company continues to report the asset on the balance sheet at $100,000. Which of the following concepts justifies this?', 'D', 20, 'The historical-cost principle.', 'The value is tied to objective and verifiable past transactions.', ' Neither of the above.', ' Both \"a\" and \"b\".'),
(93, 7, 'Retained earnings will change over time because of several factors. Which of the following factors would explain an increase in retained earnings?', 'B', 20, ' Net loss.', ' Net income.', ' Dividends.', ' Investments by stockholders.'),
(94, 8, 'Of the following account types, which would be increased by a debit?', 'C', 10, ' Liabilities and expenses.', 'Assets and equity.', ' Assets and expenses.', ' Equity and revenues.'),
(94, 9, 'The following comments all relate to the recording process. Which of these statements is correct?', 'B', 10, ' The general ledger is a chronological record of transactions.', ' The general ledger is posted from transactions recorded in the general journal.', ' The trial balance provides the primary source document for recording transactions into the general journal.', 'Transposition is the transfer of information from the general journal to the general ledger.'),
(94, 10, 'The following comments each relate to the recording of journal entries. Which statement is true?', 'D', 10, ' For any given journal entry, debits must exceed credits.', ' It is customary to record credits on the left and debits on the right.', ' The chart of accounts reveals the amount to debit and credit to the affected accounts.', ' Journalization is the process of converting transactions and events into debit/credit format.'),
(94, 11, 'Failure to record the receipt of a utility bill for services already received will result in:', 'C', 10, ' An overstatement of assets.', ' An overstatement of liabilities.', ' An overstatement of equity.', ' An understatement of assets.'),
(94, 12, 'The proper journal entry to record Ransom Company’s billing of clients for $500 of services rendered is:', 'C', 10, 'Cash   500', 'Capital Stock   500', 'Accounts Receivable   500', 'Service Revenue   500'),
(94, 13, 'The proper journal entry to record $1,000 of Dividends paid by Myer’s Corporation is:', 'A', 10, 'Dividends   1,000', 'Accounts Payable   1,000', 'Dividends Expense   1,000', 'Service Revenue   1,000'),
(94, 14, 'Lynn Lipincott invested land valued at $5,000 in her business. This transaction would be recorded by:', 'B', 10, 'Cash   5,000', 'Land   5,000', 'Service Revenue   5,000', 'Capital Stock   5,000'),
(94, 15, 'The trial balance:', 'D', 10, ' Is a formal financial statement.', ' Is used to prove that there are no errors in the journal or ledger.', ' Provides a listing of every account in the chart of accounts.', ' Provides a listing of the balance of each account in active use.'),
(101, 18, 'What is your favorite color', 'A', 12, 'red', 'green', 'blue', 'yellow');

-- --------------------------------------------------------

--
-- Table structure for table `user_details`
--

CREATE TABLE `user_details` (
  `Id` int NOT NULL,
  `First_Name` varchar(45) DEFAULT NULL,
  `Last_Name` varchar(45) DEFAULT NULL,
  `Email` varchar(255) DEFAULT NULL,
  `Password` varchar(45) DEFAULT NULL,
  `Hash` varchar(255) DEFAULT NULL,
  `Active` int DEFAULT '0',
  `role` varchar(50) DEFAULT 'learner'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `user_details`
--

INSERT INTO `user_details` (`Id`, `First_Name`, `Last_Name`, `Email`, `Password`, `Hash`, `Active`, `role`) VALUES
(11, 'TharinduCyril', 'Weerasooriya', 'tw3318@rit.edu', '9f9214c5e73f6df4f47dd82876196cfa', '5238', 1, 'admin'),
(111, 'Maheen', 'Contractor', 'mc1927@rit.edu', '9f9214c5e73f6df4f47dd82876196cfa', '6601', 1, 'admin'),
(131, 'AbdulMutalib', 'Wahaishi', 'aw@rit.edu', '9f9214c5e73f6df4f47dd82876196cfa', 'e7baf0949670d4956177af902e692465', 1, 'prof'),
(141, 'Pengchang', 'Shi', 'ps@rit.edu', '9f9214c5e73f6df4f47dd82876196cfa', 'e7baf0949670d4956177af902e692465', 1, 'prof'),
(151, 'Tim', 'Fossum', 'tf@rit.edu', '9f9214c5e73f6df4f47dd82876196cfa', 'e7baf0949670d4956177af902e692465', 1, 'admin'),
(161, 'Yin', 'Pan', 'yp@rit.edu', '9f9214c5e73f6df4f47dd82876196cfa', 'e7baf0949670d4956177af902e692465', 1, 'prof'),
(171, 'Malcolm', 'Lambrecht', 'jml1769@rit.edu', 'd5190fe93926176eb06ea4a9816e255e', 'b6004b07700fdcf5ac66167a8a980312', 1, 'prof'),
(181, 'John', 'Smith', 'js@rit.edu', '9f9214c5e73f6df4f47dd82876196cfa', 'e7baf0949670d4956177af902e692465', 1, 'prof'),
(191, 'Jill', 'Willams', 'jw@rit.edu', '9f9214c5e73f6df4f47dd82876196cfa', 'e7baf0949670d4956177af902e692465', 1, 'learner'),
(201, 'Tom', 'Smith', 'ts@rit.edu', '9f9214c5e73f6df4f47dd82876196cfa', 'e7baf0949670d4956177af902e692465', 1, 'learner'),
(211, 'Saad', 'Hassan', 'sh2513@rit.edu', '9f9214c5e73f6df4f47dd82876196cfa', 'c4bdf694ffba0c731ecb03dc242b0087', 1, 'admin'),
(221, 'Malcolm', 'Lambrecht', 'jml1769@g.rit.edu', 'd5190fe93926176eb06ea4a9816e255e', '8893', 1, 'learner'),
(241, 'Simar', 'k', 'sk8537@rit.edu', '9f9214c5e73f6df4f47dd82876196cfa', 'da52849cbe0c9f489f265e34e2e2613e', 1, 'learner'),
(271, 'Saad', 'Hassan', 'sh2513@g.rit.edu', '9f9214c5e73f6df4f47dd82876196cfa', '9546575371fbe6a873bf7971ca244901', 1, 'learner'),
(273, 'Maheen', 'Contractor', 'mc1927@g.rit.edu', '9f9214c5e73f6df4f47dd82876196cfa', '0e867c793a17c6f76486d61895383975', 1, 'learner');

-- --------------------------------------------------------

--
-- Table structure for table `user_ratings`
--

CREATE TABLE `user_ratings` (
  `userId` int NOT NULL,
  `score` int NOT NULL,
  `feedback` varchar(1250) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `user_ratings`
--

INSERT INTO `user_ratings` (`userId`, `score`, `feedback`) VALUES
(181, 4, 'Participates in class enthusiastically.'),
(181, 4, 'Completes work on time and works very hard.'),
(191, 1, 'Is always asleep in class.'),
(191, 2, 'Does not put effort into work'),
(141, 3, 'Good storyteller but slides are hard to read'),
(151, 3, 'The workload for his classes is too high. There were 15 assignments and 4 exams in this class.'),
(181, 4, 'very smart!'),
(191, 3, 'Puts in little effort into homeworks.'),
(181, 5, 'Great participation'),
(191, 4, 'She is trying to improve.'),
(191, 1, 'Puts in little effort into assignments.'),
(151, 4, 'Has very good lecture notes.'),
(191, 4, 'very smart!'),
(191, 5, 'very smart!'),
(151, 5, 'very smart!'),
(141, 4, 'Very engaging'),
(151, 4, 'good student'),
(151, 4, 'Great participation'),
(191, 1, 'Puts in little effort into homeworks.'),
(11, 4, 'very smart!'),
(151, 2, 'He is trying to improve.'),
(151, 4, 'very smart!'),
(11, 4, 'very smart!'),
(181, 1, 'Puts in little effort into homeworks.'),
(171, 5, 'Best professor ever!'),
(191, 1, 'not competent'),
(11, 5, 'Best professor ever!'),
(181, 5, 'Awesome guy'),
(151, 1, 'Boring lecture');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `courses`
--
ALTER TABLE `courses`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `courses_1_id_uindex` (`id`),
  ADD KEY `courses_user_details_Id_fk` (`profId`),
  ADD KEY `courses_courses_id_fk` (`prereq`);

--
-- Indexes for table `course_ratings`
--
ALTER TABLE `course_ratings`
  ADD KEY `course_ratings_courses_id_fk` (`course_id`);

--
-- Indexes for table `dg_members`
--
ALTER TABLE `dg_members`
  ADD KEY `dg_members_discussion_groups_id_fk` (`dg_id`),
  ADD KEY `dg_members_user_details_Id_fk` (`user_id`);

--
-- Indexes for table `discussion_groups`
--
ALTER TABLE `discussion_groups`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `discussion_groups_id_uindex` (`id`),
  ADD UNIQUE KEY `discussion_groups_course_id_uindex` (`course_id`);

--
-- Indexes for table `discussion_group_content`
--
ALTER TABLE `discussion_group_content`
  ADD PRIMARY KEY (`post_id`),
  ADD KEY `discussion_group_content_user_details_Id_fk` (`user_id`),
  ADD KEY `discussion_group_content_discussion_groups_id_fk` (`group_id`);

--
-- Indexes for table `discussion_group_requests`
--
ALTER TABLE `discussion_group_requests`
  ADD KEY `discussion_group_requests_discussion_groups_id_fk` (`dg_id`),
  ADD KEY `discussion_group_requests_user_details_Id_fk` (`user_id`);

--
-- Indexes for table `enrollments`
--
ALTER TABLE `enrollments`
  ADD PRIMARY KEY (`Id`),
  ADD KEY `enrollments_courses_id_fk` (`courseId`),
  ADD KEY `enrollments_user_details_Id_fk` (`userId`);

--
-- Indexes for table `grades`
--
ALTER TABLE `grades`
  ADD KEY `grades_courses_id_fk` (`courseId`),
  ADD KEY `grades_user_details_Id_fk` (`userId`),
  ADD KEY `grades_lessons_Id_fk` (`lessonId`),
  ADD KEY `grades_quizzes_Id_fk` (`quizId`);

--
-- Indexes for table `learning_materials`
--
ALTER TABLE `learning_materials`
  ADD KEY `learningMaterials_lessons_Id_fk` (`lessonId`);

--
-- Indexes for table `lessons`
--
ALTER TABLE `lessons`
  ADD PRIMARY KEY (`Id`),
  ADD KEY `lessons_courses_id_fk` (`courseId`);

--
-- Indexes for table `prof_requests`
--
ALTER TABLE `prof_requests`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `prof_requests_id_uindex` (`id`);

--
-- Indexes for table `question_grades`
--
ALTER TABLE `question_grades`
  ADD PRIMARY KEY (`attemptId`),
  ADD KEY `question_grades_user_details_Id_fk` (`learnerId`),
  ADD KEY `question_grades_quiz_questions_questionID_fk` (`questionId`),
  ADD KEY `question_grades_quizzes_Id_fk` (`quizId`);

--
-- Indexes for table `quizzes`
--
ALTER TABLE `quizzes`
  ADD PRIMARY KEY (`Id`),
  ADD KEY `quizzes_lessons_Id_fk` (`lessonId`);

--
-- Indexes for table `quiz_questions`
--
ALTER TABLE `quiz_questions`
  ADD PRIMARY KEY (`questionID`),
  ADD KEY `quiz_questions_quizzes_Id_fk` (`quizId`);

--
-- Indexes for table `user_details`
--
ALTER TABLE `user_details`
  ADD PRIMARY KEY (`Id`),
  ADD UNIQUE KEY `user_details_Email_uindex` (`Email`);

--
-- Indexes for table `user_ratings`
--
ALTER TABLE `user_ratings`
  ADD KEY `user_ratings_user_details_Id_fk` (`userId`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `courses`
--
ALTER TABLE `courses`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1070;

--
-- AUTO_INCREMENT for table `discussion_groups`
--
ALTER TABLE `discussion_groups`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=361;

--
-- AUTO_INCREMENT for table `discussion_group_content`
--
ALTER TABLE `discussion_group_content`
  MODIFY `post_id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=91;

--
-- AUTO_INCREMENT for table `enrollments`
--
ALTER TABLE `enrollments`
  MODIFY `Id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=343;

--
-- AUTO_INCREMENT for table `lessons`
--
ALTER TABLE `lessons`
  MODIFY `Id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=121;

--
-- AUTO_INCREMENT for table `question_grades`
--
ALTER TABLE `question_grades`
  MODIFY `attemptId` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=86;

--
-- AUTO_INCREMENT for table `quizzes`
--
ALTER TABLE `quizzes`
  MODIFY `Id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=102;

--
-- AUTO_INCREMENT for table `quiz_questions`
--
ALTER TABLE `quiz_questions`
  MODIFY `questionID` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=19;

--
-- AUTO_INCREMENT for table `user_details`
--
ALTER TABLE `user_details`
  MODIFY `Id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=274;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `courses`
--
ALTER TABLE `courses`
  ADD CONSTRAINT `courses_courses_id_fk` FOREIGN KEY (`prereq`) REFERENCES `courses` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `courses_user_details_Id_fk` FOREIGN KEY (`profId`) REFERENCES `user_details` (`Id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `course_ratings`
--
ALTER TABLE `course_ratings`
  ADD CONSTRAINT `course_ratings_courses_id_fk` FOREIGN KEY (`course_id`) REFERENCES `courses` (`id`);

--
-- Constraints for table `dg_members`
--
ALTER TABLE `dg_members`
  ADD CONSTRAINT `dg_members_discussion_groups_id_fk` FOREIGN KEY (`dg_id`) REFERENCES `discussion_groups` (`id`),
  ADD CONSTRAINT `dg_members_user_details_Id_fk` FOREIGN KEY (`user_id`) REFERENCES `user_details` (`Id`);

--
-- Constraints for table `discussion_groups`
--
ALTER TABLE `discussion_groups`
  ADD CONSTRAINT `discussion_groups_courses_id_fk` FOREIGN KEY (`course_id`) REFERENCES `courses` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `discussion_group_content`
--
ALTER TABLE `discussion_group_content`
  ADD CONSTRAINT `discussion_group_content_discussion_groups_id_fk` FOREIGN KEY (`group_id`) REFERENCES `discussion_groups` (`id`),
  ADD CONSTRAINT `discussion_group_content_user_details_Id_fk` FOREIGN KEY (`user_id`) REFERENCES `user_details` (`Id`);

--
-- Constraints for table `discussion_group_requests`
--
ALTER TABLE `discussion_group_requests`
  ADD CONSTRAINT `discussion_group_requests_discussion_groups_id_fk` FOREIGN KEY (`dg_id`) REFERENCES `discussion_groups` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `discussion_group_requests_user_details_Id_fk` FOREIGN KEY (`user_id`) REFERENCES `user_details` (`Id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `enrollments`
--
ALTER TABLE `enrollments`
  ADD CONSTRAINT `enrollments_courses_id_fk` FOREIGN KEY (`courseId`) REFERENCES `courses` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `enrollments_user_details_Id_fk` FOREIGN KEY (`userId`) REFERENCES `user_details` (`Id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `grades`
--
ALTER TABLE `grades`
  ADD CONSTRAINT `grades_courses_id_fk` FOREIGN KEY (`courseId`) REFERENCES `courses` (`id`),
  ADD CONSTRAINT `grades_lessons_Id_fk` FOREIGN KEY (`lessonId`) REFERENCES `lessons` (`Id`),
  ADD CONSTRAINT `grades_quizzes_Id_fk` FOREIGN KEY (`quizId`) REFERENCES `quizzes` (`Id`),
  ADD CONSTRAINT `grades_user_details_Id_fk` FOREIGN KEY (`userId`) REFERENCES `user_details` (`Id`);

--
-- Constraints for table `learning_materials`
--
ALTER TABLE `learning_materials`
  ADD CONSTRAINT `learningMaterials_lessons_Id_fk` FOREIGN KEY (`lessonId`) REFERENCES `lessons` (`Id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `lessons`
--
ALTER TABLE `lessons`
  ADD CONSTRAINT `lessons_courses_id_fk` FOREIGN KEY (`courseId`) REFERENCES `courses` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `prof_requests`
--
ALTER TABLE `prof_requests`
  ADD CONSTRAINT `prof_requests_user_details_Id_fk` FOREIGN KEY (`id`) REFERENCES `user_details` (`Id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `question_grades`
--
ALTER TABLE `question_grades`
  ADD CONSTRAINT `question_grades_quiz_questions_questionID_fk` FOREIGN KEY (`questionId`) REFERENCES `quiz_questions` (`questionID`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `question_grades_quizzes_Id_fk` FOREIGN KEY (`quizId`) REFERENCES `quizzes` (`Id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `question_grades_user_details_Id_fk` FOREIGN KEY (`learnerId`) REFERENCES `user_details` (`Id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `quizzes`
--
ALTER TABLE `quizzes`
  ADD CONSTRAINT `quizzes_lessons_Id_fk` FOREIGN KEY (`lessonId`) REFERENCES `lessons` (`Id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `quiz_questions`
--
ALTER TABLE `quiz_questions`
  ADD CONSTRAINT `quiz_questions_quizzes_Id_fk` FOREIGN KEY (`quizId`) REFERENCES `quizzes` (`Id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `user_ratings`
--
ALTER TABLE `user_ratings`
  ADD CONSTRAINT `user_ratings_user_details_Id_fk` FOREIGN KEY (`userId`) REFERENCES `user_details` (`Id`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
