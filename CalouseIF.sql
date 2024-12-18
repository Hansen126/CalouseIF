-- phpMyAdmin SQL Dump
-- version 5.1.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Dec 18, 2024 at 07:09 AM
-- Server version: 10.4.19-MariaDB
-- PHP Version: 8.0.7

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `calouseif`
--

-- --------------------------------------------------------

--
-- Table structure for table `items`
--

CREATE TABLE `items` (
  `itemId` varchar(15) NOT NULL,
  `itemName` varchar(100) DEFAULT NULL,
  `itemSize` varchar(50) DEFAULT NULL,
  `itemPrice` decimal(10,2) DEFAULT NULL,
  `itemCategory` varchar(50) DEFAULT NULL,
  `itemStatus` enum('Declined','Pending','Approved') DEFAULT NULL,
  `declineReason` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `items`
--

INSERT INTO `items` (`itemId`, `itemName`, `itemSize`, `itemPrice`, `itemCategory`, `itemStatus`, `declineReason`) VALUES
('ITEM001', 'Noise Cancelling Headphones', 'One Size', '299.99', 'Electronics', 'Approved', NULL),
('ITEM002', 'Standing Fan', 'Medium', '89.50', 'Home Appliances', 'Pending', NULL),
('ITEM003', 'Laptop Stand', 'Adjustable', '45.99', 'Accessories', 'Approved', NULL),
('ITEM004', 'Ergonomic Chair', 'Large', '270.00', 'Furniture', 'Declined', 'High price point'),
('ITEM005', 'Wireless Mouse', 'Compact', '35.50', 'Electronics', 'Approved', NULL),
('ITEM006', 'Gaming Keyboard', 'Full Size', '120.00', 'Electronics', 'Pending', NULL),
('ITEM007', '4K Monitor', '27 Inch', '550.00', 'Electronics', 'Approved', NULL),
('ITEM008', 'Yoga Mat', 'Standard', '25.00', 'Fitness', 'Declined', 'Low demand'),
('ITEM009', 'Cookware Set', 'Large', '180.99', 'Home Appliances', 'Approved', NULL),
('ITEM010', 'Wireless Earbuds', 'Small', '80.50', 'Electronics', 'Pending', NULL),
('ITEM011', 'Desk Lamp', 'Medium', '40.00', 'Home Decor', 'Approved', NULL),
('ITEM012', 'Leather Wallet', 'Compact', '60.00', 'Accessories', 'Declined', 'Poor quality reviews'),
('ITEM013', 'Travel Backpack', 'Large', '110.00', 'Accessories', 'Approved', NULL),
('ITEM014', 'Digital Drawing Tablet', 'Medium', '200.00', 'Electronics', 'Declined', 'too expensive'),
('ITEM015', 'Mini Projector', 'Compact', '150.00', 'Gadget', 'Approved', NULL),
('ITEM016', 'Robot Vacuum Cleaner', 'Standard', '300.00', 'Home Appliances', 'Declined', 'Technical issues'),
('ITEM017', 'Smartphone Tripod', 'Small', '28.99', 'Accessories', 'Approved', NULL),
('ITEM018', 'Camping Tent', '4 Person', '220.00', 'Outdoor', 'Pending', NULL),
('ITEM019', 'Waterproof Jacket', 'L', '70.00', 'Clothing', 'Approved', NULL),
('ITEM020', 'Bluetooth Car Adapter', 'Compact', '20.00', 'Electronics', 'Approved', NULL),
('ITEM021', 'Samsung S24', '512GB', '1000.00', 'Phone', 'Approved', NULL),
('ITEM022', 'test', 'awd', '120.00', 'test', 'Approved', NULL),
('ITEM023', 'test decline', 'testt', '100.00', 'testt', 'Declined', 'the price is too high'),
('ITEM024', 'Final Test Item', 'Final Size', '100.00', 'Final Category', 'Approved', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `offers`
--

CREATE TABLE `offers` (
  `offerId` varchar(15) NOT NULL,
  `productId` varchar(15) DEFAULT NULL,
  `buyerId` varchar(15) DEFAULT NULL,
  `offerPrice` decimal(10,2) DEFAULT NULL,
  `offerStatus` enum('Accepted','Declined','Offered') DEFAULT NULL,
  `declineReason` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `offers`
--

INSERT INTO `offers` (`offerId`, `productId`, `buyerId`, `offerPrice`, `offerStatus`, `declineReason`) VALUES
('OFFR001', 'PROD007', 'USER015', '250.00', 'Accepted', 'Item available'),
('OFFR002', 'PROD014', 'USER003', '400.00', 'Declined', 'Price too low'),
('OFFR003', 'PROD002', 'USER012', '120.00', 'Offered', NULL),
('OFFR004', 'PROD018', 'USER006', '75.50', 'Accepted', 'Mutual agreement'),
('OFFR005', 'PROD011', 'USER020', '350.00', 'Declined', 'Better offer exists'),
('OFFR006', 'PROD005', 'USER010', '95.00', 'Offered', NULL),
('OFFR007', 'PROD017', 'USER008', '500.00', 'Declined', 'Item out of stock'),
('OFFR008', 'PROD013', 'USER019', '65.00', 'Accepted', NULL),
('OFFR009', 'PROD009', 'USER002', '430.00', 'Declined', 'Too low for seller'),
('OFFR010', 'PROD016', 'USER014', '150.00', 'Offered', NULL),
('OFFR011', 'PROD022', 'USER025', '135.00', 'Declined', 'too low'),
('OFFR012', 'PROD024', 'USER026', '1000.00', 'Declined', 'too high');

-- --------------------------------------------------------

--
-- Table structure for table `products`
--

CREATE TABLE `products` (
  `productId` varchar(15) NOT NULL,
  `itemId` varchar(15) DEFAULT NULL,
  `sellerId` varchar(15) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `products`
--

INSERT INTO `products` (`productId`, `itemId`, `sellerId`) VALUES
('PROD001', 'ITEM007', 'USER012'),
('PROD002', 'ITEM005', 'USER018'),
('PROD003', 'ITEM008', 'USER003'),
('PROD004', 'ITEM002', 'USER014'),
('PROD005', 'ITEM012', 'USER017'),
('PROD006', 'ITEM015', 'USER006'),
('PROD007', 'ITEM010', 'USER019'),
('PROD008', 'ITEM018', 'USER011'),
('PROD009', 'ITEM013', 'USER004'),
('PROD010', 'ITEM006', 'USER020'),
('PROD011', 'ITEM014', 'USER002'),
('PROD012', 'ITEM017', 'USER015'),
('PROD013', 'ITEM001', 'USER008'),
('PROD014', 'ITEM016', 'USER007'),
('PROD015', 'ITEM020', 'USER005'),
('PROD016', 'ITEM003', 'USER010'),
('PROD017', 'ITEM011', 'USER013'),
('PROD018', 'ITEM009', 'USER001'),
('PROD019', 'ITEM004', 'USER016'),
('PROD020', 'ITEM019', 'USER009'),
('PROD021', 'ITEM021', 'USER002'),
('PROD022', 'ITEM022', 'USER022'),
('PROD023', 'ITEM023', 'USER022'),
('PROD024', 'ITEM024', 'USER027');

-- --------------------------------------------------------

--
-- Table structure for table `transactions`
--

CREATE TABLE `transactions` (
  `userId` varchar(15) DEFAULT NULL,
  `productId` varchar(15) DEFAULT NULL,
  `transactionId` varchar(15) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `transactions`
--

INSERT INTO `transactions` (`userId`, `productId`, `transactionId`) VALUES
('USER001', 'PROD001', 'TRSC001'),
('USER003', 'PROD002', 'TRSC003'),
('USER005', 'PROD004', 'TRSC005'),
('USER006', 'PROD003', 'TRSC007'),
('USER008', 'PROD005', 'TRSC009'),
('USER003', 'PROD001', 'TRSC010'),
('USER003', 'PROD001', 'TRSC011'),
('USER009', 'PROD003', 'TRSC012'),
('USER009', 'PROD004', 'TRSC013'),
('USER009', 'PROD006', 'TRSC014'),
('USER003', 'PROD002', 'TRSC015'),
('USER003', 'PROD004', 'TRSC016'),
('USER005', 'PROD008', 'TRSC017'),
('USER005', 'PROD006', 'TRSC018'),
('USER007', 'PROD009', 'TRSC019'),
('USER003', 'PROD001', 'TRSC020'),
('USER005', 'PROD011', 'TRSC021'),
('USER011', 'PROD014', 'TRSC022'),
('USER009', 'PROD006', 'TRSC023'),
('USER025', 'PROD022', 'TRSC024'),
('USER026', 'PROD012', 'TRSC025'),
('USER026', 'PROD024', 'TRSC026');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `userId` varchar(15) NOT NULL,
  `username` varchar(50) DEFAULT NULL,
  `password` varchar(50) DEFAULT NULL,
  `phoneNumber` varchar(20) DEFAULT NULL,
  `address` text DEFAULT NULL,
  `role` enum('seller','buyer') DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`userId`, `username`, `password`, `phoneNumber`, `address`, `role`) VALUES
('USER001', 'buyerDave', 'passwordDave', '+6281132717312', '429 NewYork St', 'buyer'),
('USER002', 'sellerDonny', 'passwordDonny', '+6282847543346', '704 Glissot St', 'seller'),
('USER003', 'buyerAlex', 'passwordAlex', '+6281749287312', '500 Maple St', 'buyer'),
('USER004', 'sellerSandy', 'passwordSandy', '+6282145783321', '123 Baker St', 'seller'),
('USER005', 'buyerLily', 'passwordLily', '+6281948274637', '145 Oakridge Ave', 'buyer'),
('USER006', 'sellerMark', 'passwordMark', '+6282745629431', '298 Kingston St', 'seller'),
('USER007', 'buyerJohn', 'passwordJohn', '+6281234567890', '391 Elmwood St', 'buyer'),
('USER008', 'sellerAnna', 'passwordAnna', '+6282567894310', '123 Pinewood Ave', 'seller'),
('USER009', 'buyerSarah', 'passwordSarah', '+6281934567432', '874 Summit Lane', 'buyer'),
('USER010', 'sellerChris', 'passwordChris', '+6282129876543', '786 Highview St', 'seller'),
('USER011', 'buyerMike', 'passwordMike', '+6281456789123', '289 Lakeside Dr', 'buyer'),
('USER012', 'sellerEmma', 'passwordEmma', '+6281267891234', '456 Sunset Blvd', 'seller'),
('USER013', 'buyerJames', 'passwordJames', '+6281937845671', '134 Westhill Rd', 'buyer'),
('USER014', 'sellerSophia', 'passwordSophia', '+6281548732619', '890 Brighton Ave', 'seller'),
('USER015', 'buyerRachel', 'passwordRachel', '+6281123568742', '947 Evergreen Rd', 'buyer'),
('USER016', 'sellerTom', 'passwordTom', '+6281734567821', '634 Grandview Ave', 'seller'),
('USER017', 'buyerKaren', 'passwordKaren', '+6281764928374', '321 Liberty St', 'buyer'),
('USER018', 'sellerLucas', 'passwordLucas', '+6281243567890', '765 Broadway Blvd', 'seller'),
('USER019', 'buyerSophia', 'passwordSophiaB', '+6281987654321', '982 Southridge St', 'buyer'),
('USER020', 'sellerOliver', 'passwordOliver', '+6281758923461', '871 Longview St', 'seller'),
('USER021', 'b', 'b', '+6281132717312', '429 NewYork St', 'buyer'),
('USER022', 's', 's', '+6282847543346', '704 Glissot St', 'seller'),
('USER023', 'Hansen', 'hansen126!', '+628117316325', 'jalan mandala', 'buyer'),
('USER024', 'HansenSeller', 'hansen126!', '+628117316325', 'jalan mandala', 'seller'),
('USER025', 'test', 'testing123!', '+62812122121212121', 'asd', 'buyer'),
('USER026', 'Final Test Buyer', 'finaltest!', '+6208131313132', 'final test address', 'buyer'),
('USER027', 'Final Test Seller', 'finaltest!', '+62128128121', 'Final test seller address', 'seller');

-- --------------------------------------------------------

--
-- Table structure for table `wishlists`
--

CREATE TABLE `wishlists` (
  `wishlistId` varchar(15) NOT NULL,
  `productId` varchar(15) DEFAULT NULL,
  `userId` varchar(15) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `wishlists`
--

INSERT INTO `wishlists` (`wishlistId`, `productId`, `userId`) VALUES
('WISH001', 'PROD010', 'USER005'),
('WISH002', 'PROD007', 'USER008'),
('WISH004', 'PROD003', 'USER003'),
('WISH005', 'PROD005', 'USER006'),
('WISH006', 'PROD004', 'USER007'),
('WISH007', 'PROD003', 'USER011'),
('WISH008', 'PROD008', 'USER002'),
('WISH009', 'PROD010', 'USER004'),
('WISH010', 'PROD006', 'USER007'),
('WISH012', 'PROD021', 'USER023'),
('WISH015', 'PROD001', 'USER026');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `items`
--
ALTER TABLE `items`
  ADD PRIMARY KEY (`itemId`);

--
-- Indexes for table `offers`
--
ALTER TABLE `offers`
  ADD PRIMARY KEY (`offerId`),
  ADD KEY `productId` (`productId`),
  ADD KEY `buyerId` (`buyerId`);

--
-- Indexes for table `products`
--
ALTER TABLE `products`
  ADD PRIMARY KEY (`productId`),
  ADD KEY `itemId` (`itemId`),
  ADD KEY `sellerId` (`sellerId`);

--
-- Indexes for table `transactions`
--
ALTER TABLE `transactions`
  ADD PRIMARY KEY (`transactionId`),
  ADD KEY `userId` (`userId`),
  ADD KEY `productId` (`productId`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`userId`);

--
-- Indexes for table `wishlists`
--
ALTER TABLE `wishlists`
  ADD PRIMARY KEY (`wishlistId`),
  ADD KEY `productId` (`productId`),
  ADD KEY `userId` (`userId`);

--
-- Constraints for dumped tables
--

--
-- Constraints for table `offers`
--
ALTER TABLE `offers`
  ADD CONSTRAINT `offers_ibfk_1` FOREIGN KEY (`productId`) REFERENCES `products` (`productId`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `offers_ibfk_2` FOREIGN KEY (`buyerId`) REFERENCES `users` (`userId`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `products`
--
ALTER TABLE `products`
  ADD CONSTRAINT `products_ibfk_1` FOREIGN KEY (`itemId`) REFERENCES `items` (`itemId`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `products_ibfk_2` FOREIGN KEY (`sellerId`) REFERENCES `users` (`userId`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `wishlists`
--
ALTER TABLE `wishlists`
  ADD CONSTRAINT `wishlists_ibfk_1` FOREIGN KEY (`productId`) REFERENCES `products` (`productId`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `wishlists_ibfk_2` FOREIGN KEY (`userId`) REFERENCES `users` (`userId`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
