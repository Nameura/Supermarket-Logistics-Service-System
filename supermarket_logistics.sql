/*
 Navicat Premium Data Transfer

 Source Server         : supermarket_logistics_service
 Source Server Type    : MySQL
 Source Server Version : 80036
 Source Host           : localhost:3306
 Source Schema         : supermarket_logistics

 Target Server Type    : MySQL
 Target Server Version : 80036
 File Encoding         : 65001

 Date: 07/05/2026 17:42:26
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for finance_record
-- ----------------------------
DROP TABLE IF EXISTS `finance_record`;
CREATE TABLE `finance_record`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '记录ID',
  `record_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '记录编号',
  `type` int(0) NOT NULL COMMENT '类型：1-收入，2-支出',
  `category` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '分类',
  `amount` decimal(10, 2) NOT NULL COMMENT '金额',
  `description` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '描述',
  `operator_id` bigint(0) DEFAULT NULL COMMENT '操作人ID',
  `status` int(0) DEFAULT 1 COMMENT '状态',
  `record_date` date DEFAULT NULL COMMENT '记录日期',
  `create_time` datetime(0) DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime(0) DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `deleted` int(0) DEFAULT 0 COMMENT '是否删除：0-否，1-是',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_finance_operator`(`operator_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 34 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '财务记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of finance_record
-- ----------------------------
INSERT INTO `finance_record` VALUES (21, 'FR20260320170648152', 2, '水费', 10086.00, '商超3月的水费支出', 2, 1, '2026-03-20', '2026-03-20 17:06:49', '2026-03-30 19:11:05', 0);
INSERT INTO `finance_record` VALUES (22, 'FR20260330182608557', 2, '采购支出', 1400.00, '采购申请[PR20240118001]审批通过 - 宝洁洗衣液 x 40，供应商：宝洁中国', 2, 1, '2026-03-30', '2026-03-30 18:26:08', '2026-03-30 18:26:08', 0);
INSERT INTO `finance_record` VALUES (23, 'FR20260330182633853', 2, '采购支出', 2100.00, '采购申请[PR20240119001]审批通过 - 三只松鼠坚果 x 60，供应商：三只松鼠', 2, 1, '2026-03-30', '2026-03-30 18:26:33', '2026-03-30 18:26:33', 0);
INSERT INTO `finance_record` VALUES (24, 'FR20260330190756000', 1, '销售商品', 16888.00, '销售商品的收入', 2, 1, '2026-03-30', '2026-03-30 19:07:56', '2026-03-30 19:07:56', 0);
INSERT INTO `finance_record` VALUES (25, 'FR20260330191047115', 2, '电费', 23333.00, '商超3月份电费支出', 2, 1, '2026-03-30', '2026-03-30 19:10:47', '2026-03-30 19:10:58', 0);
INSERT INTO `finance_record` VALUES (26, 'FR20260330191134396', 1, '摊位租金', 5800.00, '3月出租摊位收入', 2, 1, '2026-03-30', '2026-03-30 19:11:35', '2026-03-30 19:11:35', 0);
INSERT INTO `finance_record` VALUES (27, 'FR20260422113350176', 2, '采购支出', 500.00, '采购申请[PR20260422113334105]审批通过 - 红富士苹果 x 100，供应商：永辉生鲜配送', 2, 1, '2026-04-22', '2026-04-22 11:33:50', '2026-04-22 11:33:50', 0);
INSERT INTO `finance_record` VALUES (28, 'FR20260422123835750', 2, '采购支出', 500.00, '采购申请[PR20260422123814968]审批通过 - 红富士苹果 x 100，供应商：永辉生鲜配送', 2, 1, '2026-04-22', '2026-04-22 12:38:36', '2026-04-22 12:38:36', 0);
INSERT INTO `finance_record` VALUES (29, 'FR20260424211028470', 2, '采购支出', 2800.00, '采购申请[PR20260424210641647]审批通过 - 蓝月亮洗衣液 x 100，供应商：蓝月亮', 2, 1, '2026-04-24', '2026-04-24 21:10:29', '2026-04-24 21:10:29', 0);
INSERT INTO `finance_record` VALUES (30, 'FR20260424211033727', 2, '采购支出', 5950.00, '采购申请[PR20260424210756996]审批通过 - 基围虾 x 170，供应商：永辉生鲜配送', 2, 1, '2026-04-24', '2026-04-24 21:10:33', '2026-04-24 21:10:33', 0);
INSERT INTO `finance_record` VALUES (31, 'FR20260424211036237', 2, '采购支出', 27000.00, '采购申请[PR20260424210833835]审批通过 - 可口可乐 x 600，供应商：华润万家供应链', 2, 1, '2026-04-24', '2026-04-24 21:10:37', '2026-04-24 21:10:37', 0);
INSERT INTO `finance_record` VALUES (32, 'FR20260425150110905', 2, '采购支出', 750.00, '采购申请[PR20260424210618811]审批通过 - 有机胡萝卜 x 300，供应商：永辉生鲜配送', 2, 1, '2026-04-25', '2026-04-25 15:01:10', '2026-04-25 15:01:10', 0);
INSERT INTO `finance_record` VALUES (33, 'FR20260425211854440', 2, '采购支出', 1250.00, '采购申请[PR20260424211400579]审批通过 - 农夫山泉矿泉水 x 50，供应商：农夫山泉', 2, 1, '2026-04-25', '2026-04-25 21:18:54', '2026-04-25 21:18:54', 0);
INSERT INTO `finance_record` VALUES (34, 'FR20260425211854406', 2, '采购支出', 240.00, '采购申请[PR20260424211327873]审批通过 - 奥利奥饼干 x 20，供应商：华润万家供应链', 2, 1, '2026-04-25', '2026-04-25 21:18:54', '2026-04-25 21:18:54', 0);

-- ----------------------------
-- Table structure for goods
-- ----------------------------
DROP TABLE IF EXISTS `goods`;
CREATE TABLE `goods`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '商品ID',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '商品名称',
  `category_id` bigint(0) DEFAULT NULL COMMENT '分类ID',
  `barcode` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '商品条码',
  `specification` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '规格',
  `unit` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '单位',
  `purchase_price` decimal(10, 2) DEFAULT NULL COMMENT '进货价',
  `sale_price` decimal(10, 2) DEFAULT NULL COMMENT '销售价',
  `image` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '商品图片',
  `description` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '商品描述',
  `status` int(0) DEFAULT 1 COMMENT '状态：0-下架，1-上架',
  `supplier` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '供应商',
  `brand` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '品牌',
  `origin` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '产地',
  `create_time` datetime(0) DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime(0) DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `deleted` int(0) DEFAULT 0 COMMENT '是否删除：0-否，1-是',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_goods_category`(`category_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 38 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '商品表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of goods
-- ----------------------------
INSERT INTO `goods` VALUES (1, '红富士苹果', 7, '6901234567890', '约500g/个', '个', 5.00, 8.80, 'https://supermarket-logistics-service.oss-cn-beijing.aliyuncs.com/goods/40216cb7-dfde-4a74-bd63-6e055b78e5b4-20260329-红富士苹果.jpg', '好吃的苹果', 1, '永辉生鲜配送', '国产', '山东烟台', '2026-03-17 20:18:25', '2026-04-25 10:49:46', 0);
INSERT INTO `goods` VALUES (2, '进口香蕉', 7, '6901234567891', '约500g/串', '串', 3.50, 6.50, 'https://supermarket-logistics-service.oss-cn-beijing.aliyuncs.com/goods/960b4a8b-2a0e-43cd-8219-92a088838651-20260329-进口菲律宾香蕉.jpg', NULL, 0, '永辉生鲜配送', '都乐', '菲律宾', '2026-03-17 20:18:25', '2026-04-22 11:20:19', 0);
INSERT INTO `goods` VALUES (3, '新鲜西兰花', 7, '6901234567892', '约300g/颗', '颗', 3.00, 5.80, 'https://supermarket-logistics-service.oss-cn-beijing.aliyuncs.com/goods/c0b3b6b9-8c96-4b79-ac6a-fbb655b98f81-20260329-白底新鲜西兰花.jpeg', NULL, 0, '永辉生鲜配送', '国产', '云南', '2026-03-17 20:18:25', '2026-03-30 19:00:16', 0);
INSERT INTO `goods` VALUES (4, '有机胡萝卜', 7, '6901234567893', '500g/袋', '袋', 2.50, 4.50, 'https://supermarket-logistics-service.oss-cn-beijing.aliyuncs.com/goods/a398b36b-a81f-45b4-8ca3-94336736fa63-20260329-有机胡萝卜.png', NULL, 1, '永辉生鲜配送', '有机', '北京', '2026-03-17 20:18:25', '2026-03-29 19:46:57', 0);
INSERT INTO `goods` VALUES (5, '新鲜猪肉', 8, '6901234567894', '500g/盒', '盒', 18.00, 28.00, 'https://supermarket-logistics-service.oss-cn-beijing.aliyuncs.com/goods/7aba5574-2722-4118-9355-898b71199c35-20260329-新鲜猪肉.jpeg', NULL, 1, '永辉生鲜配送', '双汇', '河南', '2026-03-17 20:18:25', '2026-03-29 19:47:05', 0);
INSERT INTO `goods` VALUES (6, '鸡胸肉', 8, '6901234567895', '500g/盒', '盒', 12.00, 19.90, 'https://supermarket-logistics-service.oss-cn-beijing.aliyuncs.com/goods/2b44e011-2bb6-467d-a11d-41def4a5c190-20260329-鸡胸肉.jpeg', NULL, 1, '永辉生鲜配送', '圣农', '福建', '2026-03-17 20:18:25', '2026-03-29 19:47:10', 0);
INSERT INTO `goods` VALUES (7, '土鸡蛋', 8, '6901234567896', '30枚/盒', '盒', 25.00, 38.00, 'https://supermarket-logistics-service.oss-cn-beijing.aliyuncs.com/goods/c268bc4e-ea16-45a2-ae4b-be771d38c482-20260329-土鸡蛋.jpeg', NULL, 1, '永辉生鲜配送', '德青源', '北京', '2026-03-17 20:18:25', '2026-03-29 19:47:17', 0);
INSERT INTO `goods` VALUES (8, '伊利纯牛奶', 8, '6901234567897', '250ml*12盒', '箱', 45.00, 65.00, 'https://supermarket-logistics-service.oss-cn-beijing.aliyuncs.com/goods/1405d04a-2038-4fd2-94da-e880a735fedc-20260329-伊利纯牛奶.jpg', NULL, 1, '蒙牛乳业', '蒙牛', '内蒙古', '2026-03-17 20:18:25', '2026-03-29 19:47:29', 0);
INSERT INTO `goods` VALUES (9, '白马寺酸奶', 8, '6901234567898', '100g*8杯', '组', 15.00, 25.00, 'https://supermarket-logistics-service.oss-cn-beijing.aliyuncs.com/goods/b75ac2d2-e544-4ff3-8753-51c21861045b-20260329-白马寺酸奶.jpeg', NULL, 1, '蒙牛乳业', '蒙牛', '内蒙古', '2026-03-17 20:18:25', '2026-03-29 19:47:52', 0);
INSERT INTO `goods` VALUES (10, '大黄花鱼', 9, '6901234567899', '约500g/条', '条', 25.00, 45.00, 'https://supermarket-logistics-service.oss-cn-beijing.aliyuncs.com/goods/cac45d1c-4289-464d-9144-93effc1aa445-20260329-黄花鱼.jpeg', NULL, 1, '永辉生鲜配送', '国产', '舟山', '2026-03-17 20:18:25', '2026-03-29 19:47:59', 0);
INSERT INTO `goods` VALUES (11, '基围虾', 9, '6901234567900', '500g/盒', '盒', 35.00, 58.00, 'https://supermarket-logistics-service.oss-cn-beijing.aliyuncs.com/goods/bb26d7e9-947f-4abd-b6bf-7c16988abeb2-20260329-基围虾.jpeg', NULL, 1, '永辉生鲜配送', '国产', '广东', '2026-03-17 20:18:25', '2026-03-29 19:48:06', 0);
INSERT INTO `goods` VALUES (12, '金龙鱼调和油', 14, '6901234567901', '5L/桶', '桶', 55.00, 79.90, 'https://supermarket-logistics-service.oss-cn-beijing.aliyuncs.com/goods/2529fe7d-6ffa-4c97-bce6-867e14508518-20260329-金龙鱼调和油.png', NULL, 1, '金龙鱼食品', '金龙鱼', '上海', '2026-03-17 20:18:25', '2026-03-29 19:48:14', 0);
INSERT INTO `goods` VALUES (13, '东北大米', 14, '6901234567902', '10kg/袋', '袋', 45.00, 69.90, 'https://supermarket-logistics-service.oss-cn-beijing.aliyuncs.com/goods/58f372a8-4ac3-4e67-851f-6b37e4c8c120-20260424-东北大米.jpg', NULL, 1, '金龙鱼食品', '金龙鱼', '黑龙江', '2026-03-17 20:18:25', '2026-04-24 20:35:57', 0);
INSERT INTO `goods` VALUES (14, '海天酱油', 14, '6901234567903', '500ml/瓶', '瓶', 6.00, 12.50, 'https://supermarket-logistics-service.oss-cn-beijing.aliyuncs.com/goods/952486f2-332c-4d77-b5bb-cf3034a94e9f-20260424-海天酱油.jpg', NULL, 1, '金龙鱼食品', '海天', '广东', '2026-03-17 20:18:25', '2026-04-24 20:37:11', 0);
INSERT INTO `goods` VALUES (15, '农夫山泉矿泉水', 13, '6901234567904', '550ml*24瓶', '箱', 25.00, 36.00, 'https://supermarket-logistics-service.oss-cn-beijing.aliyuncs.com/goods/e71b2eac-b1db-4dc3-93b8-4cf5583f7a92-20260424-农夫山泉矿泉水.jpg', NULL, 1, '农夫山泉', '农夫山泉', '浙江', '2026-03-17 20:18:25', '2026-04-24 20:38:45', 0);
INSERT INTO `goods` VALUES (16, '可口可乐', 13, '6901234567905', '330ml*24罐', '箱', 45.00, 65.00, 'https://supermarket-logistics-service.oss-cn-beijing.aliyuncs.com/goods/3bc235b0-3513-46e2-8cad-00a1d6e320e1-20260424-可口可乐.jpg', NULL, 1, '华润万家供应链', '可口可乐', '上海', '2026-03-17 20:18:25', '2026-04-24 20:39:20', 0);
INSERT INTO `goods` VALUES (17, '三只松鼠坚果', 13, '6901234567906', '500g/袋', '袋', 35.00, 59.90, 'https://supermarket-logistics-service.oss-cn-beijing.aliyuncs.com/goods/850ebc9e-d358-4005-b121-d54f09e7b584-20260424-三只松鼠坚果.png', NULL, 1, '三只松鼠', '三只松鼠', '安徽', '2026-03-17 20:18:25', '2026-04-24 20:40:49', 0);
INSERT INTO `goods` VALUES (18, '奥利奥饼干', 13, '6901234567907', '388g/盒', '盒', 12.00, 19.90, 'https://supermarket-logistics-service.oss-cn-beijing.aliyuncs.com/goods/e29cb0c7-1aca-473e-ac45-40fd14ab3321-20260424-奥利奥饼干.jpg', NULL, 1, '华润万家供应链', '奥利奥', '上海', '2026-03-17 20:18:25', '2026-04-24 20:42:12', 0);
INSERT INTO `goods` VALUES (19, '宝洁洗衣液', 12, '6901234567908', '3kg/桶', '桶', 35.00, 49.90, 'https://supermarket-logistics-service.oss-cn-beijing.aliyuncs.com/goods/ed2cb7f2-fcaa-42e6-a216-652d04e8b6ba-20260424-宝洁洗衣液.jpeg', NULL, 1, '宝洁中国', '汰渍', '广州', '2026-03-17 20:18:25', '2026-04-24 20:45:09', 0);
INSERT INTO `goods` VALUES (20, '舒肤佳香皂', 12, '6901234567909', '125g*3块', '组', 12.00, 19.90, 'https://supermarket-logistics-service.oss-cn-beijing.aliyuncs.com/goods/61ede535-cce6-4b0d-bea8-420da7a7da18-20260424-舒肤佳香皂.jpg', NULL, 1, '宝洁中国', '舒肤佳', '广州', '2026-03-17 20:18:25', '2026-04-24 20:45:41', 0);
INSERT INTO `goods` VALUES (21, '蓝月亮洗手液', 12, '6901234567910', '500ml/瓶', '瓶', 12.00, 18.90, 'https://supermarket-logistics-service.oss-cn-beijing.aliyuncs.com/goods/0a13f324-cbfb-4136-8895-fbcba93a4934-20260424-蓝月亮洗手液.jpeg', NULL, 1, '蓝月亮', '蓝月亮', '广州', '2026-03-17 20:18:25', '2026-04-24 20:46:21', 0);
INSERT INTO `goods` VALUES (22, '清风抽纸', 12, '6901234567911', '100抽*10包', '提', 25.00, 39.90, 'https://supermarket-logistics-service.oss-cn-beijing.aliyuncs.com/goods/833205d1-683a-4940-900f-1b8dec56fde2-20260424-清风抽纸.jpg', NULL, 1, '联合利华', '清风', '上海', '2026-03-17 20:18:25', '2026-04-24 20:47:21', 0);
INSERT INTO `goods` VALUES (23, '中华牙膏', 12, '6901234567912', '120g/支', '支', 8.00, 15.90, 'https://supermarket-logistics-service.oss-cn-beijing.aliyuncs.com/goods/e8715c79-0ec1-4662-a286-f6a01b34b7d8-20260424-中华牙膏.jpeg', NULL, 1, '联合利华', '中华', '上海', '2026-03-17 20:18:25', '2026-04-24 20:48:45', 0);
INSERT INTO `goods` VALUES (24, '高露洁牙刷', 12, '6901234567913', '1支/盒', '盒', 8.00, 15.90, 'https://supermarket-logistics-service.oss-cn-beijing.aliyuncs.com/goods/24f1f4b6-7630-4acc-bca0-92327eaf37da-20260424-高露洁牙刷.jpg', NULL, 1, '联合利华', '高露洁', '上海', '2026-03-17 20:18:25', '2026-04-24 20:49:24', 0);
INSERT INTO `goods` VALUES (25, '得力中性笔', 13, '6901234567914', '0.5mm*12支', '盒', 8.00, 15.00, 'https://supermarket-logistics-service.oss-cn-beijing.aliyuncs.com/goods/8f826bfe-4782-46df-a12e-d118512fb5c1-20260424-得力中性笔.jpg', NULL, 1, '华润万家供应链', '得力', '浙江', '2026-03-17 20:18:25', '2026-04-24 20:49:57', 0);
INSERT INTO `goods` VALUES (26, 'A4打印纸', 13, '6901234567915', '70g*500张', '包', 18.00, 28.00, 'https://supermarket-logistics-service.oss-cn-beijing.aliyuncs.com/goods/1c5c134f-15ad-4a14-9693-3b3d778742dd-20260424-A4打印纸.jpg', NULL, 1, '华润万家供应链', '得力', '浙江', '2026-03-17 20:18:25', '2026-04-24 20:50:40', 0);
INSERT INTO `goods` VALUES (27, '婴儿纸尿裤', 4, '6901234567916', 'M码*52片', '包', 65.00, 99.00, 'https://supermarket-logistics-service.oss-cn-beijing.aliyuncs.com/goods/aee17952-6e1d-4b70-a0e9-bf555cbfd7dd-20260424-婴儿纸尿裤.jpg', NULL, 1, '华润万家供应链', '帮宝适', '广州', '2026-03-17 20:18:25', '2026-04-24 20:51:18', 0);
INSERT INTO `goods` VALUES (28, '婴儿奶粉', 4, '6901234567917', '900g/罐', '罐', 180.00, 268.00, 'https://supermarket-logistics-service.oss-cn-beijing.aliyuncs.com/goods/421c6275-41cd-498e-a9f0-24b8694c7eae-20260424-婴儿奶粉.jpg', NULL, 1, '华润万家供应链', '美赞臣', '荷兰', '2026-03-17 20:18:25', '2026-04-24 20:51:48', 0);
INSERT INTO `goods` VALUES (29, '婴儿湿巾', 4, '6901234567918', '80抽/包', '包', 15.00, 25.00, 'https://supermarket-logistics-service.oss-cn-beijing.aliyuncs.com/goods/a901a98f-c24a-47f8-b183-ff3a7ac1aec1-20260424-婴儿湿巾.jpeg', NULL, 1, '华润万家供应链', '好奇', '美国', '2026-03-17 20:18:25', '2026-04-24 20:52:23', 0);
INSERT INTO `goods` VALUES (30, '洗发水', 5, '6901234567919', '750ml/瓶', '瓶', 35.00, 59.90, 'https://supermarket-logistics-service.oss-cn-beijing.aliyuncs.com/goods/08a2f115-42a4-4b76-adf2-f1d74a942974-20260424-海飞丝洗发水.jpg', NULL, 1, '宝洁中国', '海飞丝', '广州', '2026-03-17 20:18:25', '2026-04-24 20:53:11', 0);
INSERT INTO `goods` VALUES (31, '沐浴露', 5, '6901234567920', '750ml/瓶', '瓶', 30.00, 49.90, 'https://supermarket-logistics-service.oss-cn-beijing.aliyuncs.com/goods/c6fe0388-e347-42a8-929a-1bedb0fcff13-20260424-多芬沐浴露.jpg', NULL, 1, '联合利华', '多芬', '上海', '2026-03-17 20:18:25', '2026-04-24 20:53:47', 0);
INSERT INTO `goods` VALUES (32, '护肤霜', 5, '6901234567921', '50g/瓶', '瓶', 45.00, 79.90, 'https://supermarket-logistics-service.oss-cn-beijing.aliyuncs.com/goods/21211c18-3dd1-4c10-88d7-ff123d8031ce-20260424-凡士林护肤霜.jpg', NULL, 1, '联合利华', '凡士林', '上海', '2026-03-17 20:18:25', '2026-04-24 20:54:46', 0);
INSERT INTO `goods` VALUES (33, '洗洁精', 6, '6901234567922', '1kg/瓶', '瓶', 10.00, 16.90, 'https://supermarket-logistics-service.oss-cn-beijing.aliyuncs.com/goods/8ede6d09-e2a1-4157-b8f6-a3fef18ea149-20260424-蓝月亮洗洁精.jpg', NULL, 1, '蓝月亮', '蓝月亮', '广州', '2026-03-17 20:18:25', '2026-04-24 20:55:58', 0);
INSERT INTO `goods` VALUES (34, '洁厕灵', 6, '6901234567923', '500ml/瓶', '瓶', 8.00, 12.90, 'https://supermarket-logistics-service.oss-cn-beijing.aliyuncs.com/goods/f6c20a51-8d6a-4ae2-a205-77eb2ee1483d-20260424-威猛先生洁厕灵.jpg', NULL, 1, '蓝月亮', '威猛先生', '广州', '2026-03-17 20:18:25', '2026-04-24 20:56:32', 0);
INSERT INTO `goods` VALUES (35, '垃圾袋', 6, '6901234567924', '中号*100只', '卷', 8.00, 15.00, NULL, NULL, 0, '华润万家供应链', '妙洁', '台湾', '2026-03-17 20:18:25', '2026-04-24 20:59:03', 0);
INSERT INTO `goods` VALUES (36, '蓝月亮洗衣液', 6, '15611281281', '2kg', '桶', 28.00, 35.00, 'https://supermarket-logistics-service.oss-cn-beijing.aliyuncs.com/goods/0663178a-e357-40c6-8d53-2af225e53996-20260422-蓝月亮洗衣液.jpeg', '', 1, '蓝月亮', '蓝月亮', '', '2026-03-22 17:56:00', '2026-04-22 11:29:00', 0);
INSERT INTO `goods` VALUES (37, '测试商品', 6, '114514', '1', '个', 50.00, 100.00, 'https://supermarket-logistics-service.oss-cn-beijing.aliyuncs.com/goods/67054228-26a2-4aa0-b14b-9403816144ce-20260422-这么强.jpg', '', 0, '华润万家供应链', '', '越南', '2026-04-22 11:21:24', '2026-04-22 11:21:51', 1);
INSERT INTO `goods` VALUES (38, '测试', 6, '', '', '', 3.00, 1.00, '', '', 0, '', '', '', '2026-04-24 20:59:21', '2026-04-24 20:59:32', 1);

-- ----------------------------
-- Table structure for goods_category
-- ----------------------------
DROP TABLE IF EXISTS `goods_category`;
CREATE TABLE `goods_category`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '分类ID',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '分类名称',
  `parent_id` bigint(0) DEFAULT 0 COMMENT '父分类ID',
  `sort` int(0) DEFAULT 0 COMMENT '排序',
  `icon` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '图标',
  `status` int(0) DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
  `description` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '分类描述',
  `create_time` datetime(0) DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime(0) DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `deleted` int(0) DEFAULT 0 COMMENT '是否删除：0-否，1-是',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '商品分类表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of goods_category
-- ----------------------------
INSERT INTO `goods_category` VALUES (1, '生鲜食品', 0, 1, NULL, 1, '新鲜食品类', '2026-03-17 20:18:25', '2026-03-17 20:18:25', 0);
INSERT INTO `goods_category` VALUES (2, '日用百货', 0, 2, NULL, 1, '日常生活用品', '2026-03-17 20:18:25', '2026-03-17 20:18:25', 0);
INSERT INTO `goods_category` VALUES (3, '食品饮料', 0, 3, NULL, 1, '食品和饮料类', '2026-03-17 20:18:25', '2026-03-17 20:18:25', 0);
INSERT INTO `goods_category` VALUES (4, '母婴用品', 0, 4, NULL, 1, '母婴相关产品', '2026-03-17 20:18:25', '2026-03-17 20:18:25', 0);
INSERT INTO `goods_category` VALUES (5, '个护美妆', 0, 5, NULL, 1, '个人护理和美妆产品', '2026-03-17 20:18:25', '2026-03-17 20:18:25', 0);
INSERT INTO `goods_category` VALUES (6, '家居清洁', 0, 6, NULL, 1, '家居清洁用品', '2026-03-17 20:18:25', '2026-03-17 20:18:25', 0);
INSERT INTO `goods_category` VALUES (7, '蔬菜水果', 1, 1, NULL, 1, '新鲜蔬菜和水果', '2026-03-17 20:18:25', '2026-03-17 20:18:25', 0);
INSERT INTO `goods_category` VALUES (8, '肉禽蛋奶', 1, 2, NULL, 1, '肉类、禽类、蛋类和奶制品', '2026-03-17 20:18:25', '2026-03-17 20:18:25', 0);
INSERT INTO `goods_category` VALUES (9, '海鲜水产', 1, 3, NULL, 1, '海鲜和水产品', '2026-03-17 20:18:25', '2026-03-17 20:18:25', 0);
INSERT INTO `goods_category` VALUES (10, '厨房用品', 2, 1, NULL, 1, '厨房相关用品', '2026-03-17 20:18:25', '2026-03-17 20:18:25', 0);
INSERT INTO `goods_category` VALUES (11, '洗护用品', 2, 2, NULL, 1, '洗涤护理用品', '2026-03-17 20:18:25', '2026-03-17 20:18:25', 0);
INSERT INTO `goods_category` VALUES (12, '文具办公', 2, 3, NULL, 1, '文具和办公用品', '2026-03-17 20:18:25', '2026-03-17 20:18:25', 0);
INSERT INTO `goods_category` VALUES (13, '零食饮料', 3, 1, NULL, 1, '零食和饮料', '2026-03-17 20:18:25', '2026-03-17 20:18:25', 0);
INSERT INTO `goods_category` VALUES (14, '粮油调味', 3, 2, NULL, 1, '粮油和调味品', '2026-03-17 20:18:25', '2026-03-17 20:18:25', 0);
INSERT INTO `goods_category` VALUES (15, '测试', 0, 1, NULL, 0, '', '2026-03-17 21:26:47', '2026-04-22 11:18:22', 1);
INSERT INTO `goods_category` VALUES (16, '测试', 8, 1, NULL, 1, '', '2026-04-22 11:18:52', '2026-04-22 11:20:03', 1);

-- ----------------------------
-- Table structure for inventory
-- ----------------------------
DROP TABLE IF EXISTS `inventory`;
CREATE TABLE `inventory`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '库存ID',
  `goods_id` bigint(0) NOT NULL COMMENT '商品ID',
  `quantity` int(0) DEFAULT 0 COMMENT '库存数量',
  `warning_quantity` int(0) DEFAULT 10 COMMENT '预警数量',
  `warehouse` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '仓库',
  `location` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '库位',
  `status` int(0) DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
  `create_time` datetime(0) DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime(0) DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `deleted` int(0) DEFAULT 0 COMMENT '是否删除：0-否，1-是',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_inventory_goods`(`goods_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 37 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '库存表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of inventory
-- ----------------------------
INSERT INTO `inventory` VALUES (1, 1, 120, 50, '一号仓库', 'A-01-01', 1, '2026-03-17 20:18:25', '2026-04-25 09:58:20', 0);
INSERT INTO `inventory` VALUES (2, 2, 20, 50, '一号仓库', 'A-01-02', 1, '2026-03-17 20:18:25', '2026-04-22 17:09:42', 0);
INSERT INTO `inventory` VALUES (3, 3, 80, 30, '一号仓库', 'A-02-01', 1, '2026-03-17 20:18:25', '2026-03-17 20:18:25', 0);
INSERT INTO `inventory` VALUES (4, 4, 420, 40, '一号仓库', 'A-02-02', 1, '2026-03-17 20:18:25', '2026-03-17 20:18:25', 0);
INSERT INTO `inventory` VALUES (5, 5, 50, 20, '一号仓库', 'B-01-01', 1, '2026-03-17 20:18:25', '2026-03-17 20:18:25', 0);
INSERT INTO `inventory` VALUES (6, 6, 60, 25, '一号仓库', 'B-01-02', 1, '2026-03-17 20:18:25', '2026-03-17 20:18:25', 0);
INSERT INTO `inventory` VALUES (7, 7, 100, 30, '一号仓库', 'B-02-01', 1, '2026-03-17 20:18:25', '2026-03-17 20:18:25', 0);
INSERT INTO `inventory` VALUES (8, 8, 180, 50, '二号仓库', 'C-01-01', 1, '2026-03-17 20:18:25', '2026-03-17 20:18:25', 0);
INSERT INTO `inventory` VALUES (9, 9, 150, 40, '二号仓库', 'C-01-02', 1, '2026-03-17 20:18:25', '2026-03-17 20:18:25', 0);
INSERT INTO `inventory` VALUES (10, 10, 30, 20, '一号仓库', 'D-01-01', 1, '2026-03-17 20:18:25', '2026-03-17 20:18:25', 0);
INSERT INTO `inventory` VALUES (11, 11, 180, 15, '一号仓库', 'D-01-02', 1, '2026-03-17 20:18:25', '2026-03-30 16:15:49', 0);
INSERT INTO `inventory` VALUES (12, 12, 200, 50, '二号仓库', 'E-01-01', 1, '2026-03-17 20:18:25', '2026-03-17 20:18:25', 0);
INSERT INTO `inventory` VALUES (13, 13, 300, 100, '二号仓库', 'E-01-02', 1, '2026-03-17 20:18:25', '2026-03-17 20:18:25', 0);
INSERT INTO `inventory` VALUES (14, 14, 250, 80, '二号仓库', 'E-02-01', 1, '2026-03-17 20:18:25', '2026-03-17 20:18:25', 0);
INSERT INTO `inventory` VALUES (15, 15, 550, 100, '二号仓库', 'F-01-01', 1, '2026-03-17 20:18:25', '2026-03-17 20:18:25', 0);
INSERT INTO `inventory` VALUES (16, 16, 1000, 100, '二号仓库', 'F-01-02', 1, '2026-03-17 20:18:25', '2026-03-17 20:18:25', 0);
INSERT INTO `inventory` VALUES (17, 17, 80, 30, '二号仓库', 'F-02-01', 1, '2026-03-17 20:18:25', '2026-03-17 20:18:25', 0);
INSERT INTO `inventory` VALUES (18, 18, 140, 40, '二号仓库', 'F-02-02', 1, '2026-03-17 20:18:25', '2026-03-17 20:18:25', 0);
INSERT INTO `inventory` VALUES (19, 19, 150, 50, '三号仓库', 'G-01-01', 1, '2026-03-17 20:18:25', '2026-03-17 20:18:25', 0);
INSERT INTO `inventory` VALUES (20, 20, 200, 60, '三号仓库', 'G-01-02', 1, '2026-03-17 20:18:25', '2026-03-17 20:18:25', 0);
INSERT INTO `inventory` VALUES (21, 21, 180, 50, '三号仓库', 'G-02-01', 1, '2026-03-17 20:18:25', '2026-03-17 20:18:25', 0);
INSERT INTO `inventory` VALUES (22, 22, 250, 80, '三号仓库', 'G-02-02', 1, '2026-03-17 20:18:25', '2026-03-17 20:18:25', 0);
INSERT INTO `inventory` VALUES (23, 23, 300, 100, '三号仓库', 'H-01-01', 1, '2026-03-17 20:18:25', '2026-03-17 20:18:25', 0);
INSERT INTO `inventory` VALUES (24, 24, 280, 80, '三号仓库', 'H-01-02', 1, '2026-03-17 20:18:25', '2026-03-17 20:18:25', 0);
INSERT INTO `inventory` VALUES (25, 25, 150, 50, '三号仓库', 'H-02-01', 1, '2026-03-17 20:18:25', '2026-03-17 20:18:25', 0);
INSERT INTO `inventory` VALUES (26, 26, 100, 30, '三号仓库', 'I-01-01', 1, '2026-03-17 20:18:25', '2026-03-17 20:18:25', 0);
INSERT INTO `inventory` VALUES (27, 27, 80, 30, '三号仓库', 'I-01-02', 1, '2026-03-17 20:18:25', '2026-03-17 20:18:25', 0);
INSERT INTO `inventory` VALUES (28, 28, 60, 20, '三号仓库', 'I-02-01', 1, '2026-03-17 20:18:25', '2026-03-17 20:18:25', 0);
INSERT INTO `inventory` VALUES (29, 29, 90, 30, '三号仓库', 'I-02-02', 1, '2026-03-17 20:18:25', '2026-03-17 20:18:25', 0);
INSERT INTO `inventory` VALUES (30, 30, 120, 40, '三号仓库', 'J-01-01', 1, '2026-03-17 20:18:25', '2026-03-17 20:18:25', 0);
INSERT INTO `inventory` VALUES (31, 31, 100, 30, '三号仓库', 'J-01-02', 1, '2026-03-17 20:18:25', '2026-03-17 20:18:25', 0);
INSERT INTO `inventory` VALUES (32, 32, 80, 25, '三号仓库', 'J-02-01', 1, '2026-03-17 20:18:25', '2026-03-17 20:18:25', 0);
INSERT INTO `inventory` VALUES (33, 33, 200, 60, '三号仓库', 'J-02-02', 1, '2026-03-17 20:18:25', '2026-03-17 20:18:25', 0);
INSERT INTO `inventory` VALUES (34, 34, 180, 50, '三号仓库', 'K-01-01', 1, '2026-03-17 20:18:25', '2026-03-17 20:18:25', 0);
INSERT INTO `inventory` VALUES (35, 35, 250, 80, '三号仓库', 'K-01-02', 1, '2026-03-17 20:18:25', '2026-03-17 20:18:25', 0);
INSERT INTO `inventory` VALUES (36, 36, 50, 10, '二号仓库', 'A-03-23', 1, '2026-03-22 17:58:02', '2026-04-22 11:29:19', 1);
INSERT INTO `inventory` VALUES (37, 36, 300, 10, '一号仓库', 'A-03-23', 1, '2026-04-22 11:29:34', '2026-04-22 11:29:34', 0);

-- ----------------------------
-- Table structure for purchase_request
-- ----------------------------
DROP TABLE IF EXISTS `purchase_request`;
CREATE TABLE `purchase_request`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '采购请求ID',
  `request_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '采购单号',
  `goods_id` bigint(0) NOT NULL COMMENT '商品ID',
  `supplier_id` bigint(0) DEFAULT NULL COMMENT '供应商ID',
  `quantity` int(0) NOT NULL COMMENT '采购数量',
  `unit_price` decimal(10, 2) DEFAULT NULL COMMENT '单价',
  `total_price` decimal(10, 2) DEFAULT NULL COMMENT '总价',
  `approval_status` int(0) DEFAULT 0 COMMENT '审批状态：0-待审批，1-已通过，2-已拒绝',
  `applicant_id` bigint(0) DEFAULT NULL COMMENT '申请人ID',
  `approver_id` bigint(0) DEFAULT NULL COMMENT '审批人ID',
  `approval_remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '审批备注',
  `approval_time` datetime(0) DEFAULT NULL COMMENT '审批时间',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  `status` int(0) DEFAULT 1 COMMENT '状态',
  `create_time` datetime(0) DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime(0) DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `deleted` int(0) DEFAULT 0 COMMENT '是否删除：0-否，1-是',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_purchase_goods`(`goods_id`) USING BTREE,
  INDEX `idx_purchase_supplier`(`supplier_id`) USING BTREE,
  INDEX `idx_purchase_applicant`(`applicant_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 23 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '采购请求表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of purchase_request
-- ----------------------------
INSERT INTO `purchase_request` VALUES (11, 'PR20260317205457305', 1, 2, 120, 5.00, 600.00, 1, 5, 2, '测试通过', '2026-03-17 20:57:09', '测试采购苹果', 1, '2026-03-17 20:54:58', '2026-03-17 20:54:58', 0);
INSERT INTO `purchase_request` VALUES (12, 'PR20260409185932931', 6, 2, 100, 8.00, 800.00, 2, 5, 2, '时限不合规', '2026-04-24 21:10:59', '新鲜鸡胸肉', 1, '2026-04-09 18:59:32', '2026-04-09 18:59:32', 0);
INSERT INTO `purchase_request` VALUES (13, 'PR20260422113334105', 1, 2, 100, 5.00, 500.00, 1, 5, 2, '测试通过', '2026-04-22 11:33:50', '测试采购', 1, '2026-04-22 11:33:34', '2026-04-22 11:33:34', 0);
INSERT INTO `purchase_request` VALUES (14, 'PR20260422123814968', 1, 2, 100, 5.00, 500.00, 1, 5, 2, '', '2026-04-22 12:38:36', '', 1, '2026-04-22 12:38:15', '2026-04-22 12:38:15', 0);
INSERT INTO `purchase_request` VALUES (15, 'PR20260424210618811', 4, 2, 300, 2.50, 750.00, 1, 5, 2, '', '2026-04-25 15:01:10', '冷库胡萝卜补货', 1, '2026-04-24 21:06:18', '2026-04-24 21:06:18', 0);
INSERT INTO `purchase_request` VALUES (16, 'PR20260424210641647', 36, 10, 100, 28.00, 2800.00, 1, 5, 2, '批准', '2026-04-24 21:10:29', '蓝月亮品牌促销', 1, '2026-03-05 21:06:41', '2026-04-24 21:09:15', 0);
INSERT INTO `purchase_request` VALUES (17, 'PR20260424210713357', 8, 1, 300, 45.00, 13500.00, 0, 5, NULL, NULL, NULL, '伊利五一大促', 1, '2026-04-24 21:07:14', '2026-04-24 21:07:14', 0);
INSERT INTO `purchase_request` VALUES (18, 'PR20260424210756996', 11, 2, 170, 35.00, 5950.00, 1, 5, 2, '批准', '2026-04-24 21:10:33', '冷链基围虾补货', 1, '2026-02-12 21:07:56', '2026-04-24 21:09:15', 0);
INSERT INTO `purchase_request` VALUES (19, 'PR20260424210833835', 16, 1, 600, 45.00, 27000.00, 1, 5, 2, '批准', '2026-04-24 21:10:37', '春节补货', 1, '2026-01-24 21:08:33', '2026-04-24 21:09:15', 0);
INSERT INTO `purchase_request` VALUES (20, 'PR20260424211327873', 18, 1, 20, 12.00, 240.00, 1, 5, 2, '批量审批 - 批量审批通过', '2026-04-25 21:18:54', '限时限量促销', 1, '2026-04-24 21:13:28', '2026-04-24 21:13:28', 0);
INSERT INTO `purchase_request` VALUES (21, 'PR20260424211400579', 15, 7, 50, 25.00, 1250.00, 1, 5, 2, '批量审批 - 批量审批通过', '2026-04-25 21:18:54', '满赠商品', 1, '2026-04-24 21:14:01', '2026-04-24 21:14:01', 0);
INSERT INTO `purchase_request` VALUES (22, 'PR20260425211920327', 4, 2, 100, 2.50, 250.00, 0, 5, NULL, NULL, NULL, '测试批量审批', 1, '2026-04-25 21:19:21', '2026-04-25 21:19:21', 0);
INSERT INTO `purchase_request` VALUES (23, 'PR20260425211935462', 7, 2, 150, 25.00, 3750.00, 0, 5, NULL, NULL, NULL, '测试批量审批', 1, '2026-04-25 21:19:36', '2026-04-25 21:19:36', 0);

-- ----------------------------
-- Table structure for schedule
-- ----------------------------
DROP TABLE IF EXISTS `schedule`;
CREATE TABLE `schedule`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '排班ID',
  `user_id` bigint(0) NOT NULL COMMENT '用户ID',
  `work_date` datetime(0) NOT NULL COMMENT '工作日期',
  `shift` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '班次',
  `start_time` datetime(0) DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime(0) DEFAULT NULL COMMENT '结束时间',
  `status` int(0) DEFAULT 1 COMMENT '状态：0-休息，1-上班',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  `creator_id` bigint(0) DEFAULT NULL COMMENT '创建人ID',
  `create_time` datetime(0) DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime(0) DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `deleted` int(0) DEFAULT 0 COMMENT '是否删除：0-否，1-是',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_schedule_user`(`user_id`) USING BTREE,
  INDEX `idx_schedule_date`(`work_date`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 29 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '排班表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of schedule
-- ----------------------------
INSERT INTO `schedule` VALUES (1, 5, '2026-04-02 00:00:00', '早班', '2026-04-02 08:00:00', '2026-04-02 16:00:00', 1, '正常上班', 2, '2026-03-17 20:18:25', '2026-04-07 12:38:39', 0);
INSERT INTO `schedule` VALUES (2, 6, '2026-01-15 00:00:00', '早班', '2026-01-15 08:00:00', '2026-01-15 16:00:00', 1, '正常上班', 2, '2026-03-17 20:18:25', '2026-04-07 12:46:18', 0);
INSERT INTO `schedule` VALUES (3, 7, '2026-01-15 00:00:00', '晚班', '2026-01-15 16:00:00', '2026-01-15 22:00:00', 1, '正常上班', 2, '2026-03-17 20:18:25', '2026-04-07 12:46:38', 0);
INSERT INTO `schedule` VALUES (4, 8, '2026-01-15 00:00:00', '中班', '2026-01-15 08:00:00', '2026-01-15 16:00:00', 1, '正常上班', 2, '2026-03-17 20:18:25', '2026-04-07 12:46:23', 0);
INSERT INTO `schedule` VALUES (5, 9, '2026-01-15 00:00:00', '晚班', '2026-01-15 16:00:00', '2026-01-15 22:00:00', 1, '正常上班', 2, '2026-03-17 20:18:25', '2026-04-07 12:46:43', 0);
INSERT INTO `schedule` VALUES (6, 10, '2026-04-01 00:00:00', '早班', '2026-04-01 06:00:00', '2026-04-01 14:00:00', 1, '生鲜区早班', 2, '2026-03-17 20:18:25', '2026-04-07 12:38:28', 0);
INSERT INTO `schedule` VALUES (7, 11, '2026-01-15 00:00:00', '晚班', '2026-01-15 14:00:00', '2026-01-15 22:00:00', 1, '百货区晚班', 2, '2026-03-17 20:18:25', '2026-04-07 12:46:29', 0);
INSERT INTO `schedule` VALUES (8, 12, '2026-01-15 00:00:00', '早班', '2026-01-15 08:00:00', '2026-01-15 16:00:00', 1, '正常上班', 2, '2026-03-17 20:18:25', '2026-04-07 12:46:34', 0);
INSERT INTO `schedule` VALUES (9, 5, '2026-02-04 00:00:00', '晚班', '2026-02-04 16:00:00', '2026-02-04 22:00:00', 1, '正常上班', 2, '2026-03-17 20:18:25', '2026-04-07 12:45:14', 0);
INSERT INTO `schedule` VALUES (10, 6, '2026-02-04 00:00:00', '晚班', '2026-02-04 16:00:00', '2026-02-04 22:00:00', 1, '正常上班', 2, '2026-03-17 20:18:25', '2026-04-07 12:45:20', 0);
INSERT INTO `schedule` VALUES (11, 7, '2026-01-16 00:00:00', '早班', '2026-01-16 08:00:00', '2026-01-16 16:00:00', 1, '正常上班', 2, '2026-03-17 20:18:25', '2026-04-07 12:46:48', 0);
INSERT INTO `schedule` VALUES (12, 8, '2026-03-06 00:00:00', '晚班', '2026-03-06 16:00:00', '2026-03-06 22:00:00', 1, '正常上班', 2, '2026-03-17 20:18:25', '2026-04-07 12:45:27', 0);
INSERT INTO `schedule` VALUES (13, 9, '2026-01-16 00:00:00', '早班', '2026-01-16 08:00:00', '2026-01-16 16:00:00', 1, '正常上班', 2, '2026-03-17 20:18:25', '2026-04-07 12:46:53', 0);
INSERT INTO `schedule` VALUES (14, 10, '2026-01-17 00:00:00', '晚班', '2026-01-17 14:00:00', '2026-01-17 22:00:00', 1, '生鲜区晚班', 2, '2026-03-17 20:18:25', '2026-04-07 12:45:05', 0);
INSERT INTO `schedule` VALUES (15, 11, '2026-01-16 00:00:00', '早班', '2026-01-16 08:00:00', '2026-01-16 16:00:00', 1, '百货区早班', 2, '2026-03-17 20:18:25', '2026-04-07 12:46:04', 0);
INSERT INTO `schedule` VALUES (16, 12, '2026-01-14 00:00:00', '晚班', '2026-01-14 16:00:00', '2026-01-14 22:00:00', 1, '正常上班', 2, '2026-03-17 20:18:25', '2026-04-07 12:45:36', 0);
INSERT INTO `schedule` VALUES (17, 5, '2026-01-17 00:00:00', '早班', '2026-01-17 08:00:00', '2026-01-17 16:00:00', 1, '正常上班', 2, '2026-03-17 20:18:25', '2026-04-07 12:45:42', 0);
INSERT INTO `schedule` VALUES (18, 6, '2026-01-17 00:00:00', '早班', '2026-01-17 08:00:00', '2026-01-17 16:00:00', 1, '正常上班', 2, '2026-03-17 20:18:25', '2026-04-07 12:45:47', 0);
INSERT INTO `schedule` VALUES (19, 7, '2026-01-17 00:00:00', '晚班', '2026-01-17 16:00:00', '2026-01-17 22:00:00', 1, '正常上班', 2, '2026-03-17 20:18:25', '2026-04-07 12:45:57', 0);
INSERT INTO `schedule` VALUES (20, 8, '2026-01-17 00:00:00', '早班', '2026-01-17 08:00:00', '2026-01-17 16:00:00', 1, '正常上班', 2, '2026-03-17 20:18:25', '2026-04-07 12:45:52', 0);
INSERT INTO `schedule` VALUES (21, 5, '2026-03-17 00:00:00', '晚班', '2026-03-17 10:00:00', '2026-03-17 14:00:00', 0, '', 2, '2026-03-17 21:13:34', '2026-03-20 17:48:23', 0);
INSERT INTO `schedule` VALUES (22, 5, '2026-03-20 00:00:00', '晚班', '2026-03-20 08:00:08', '2026-03-20 23:00:08', 1, '测试', 2, '2026-03-20 17:14:38', '2026-03-20 17:48:19', 0);
INSERT INTO `schedule` VALUES (23, 5, '2025-12-27 00:00:00', '中班', '2025-12-27 12:47:14', '2025-12-27 21:47:14', 1, '中午上班', 2, '2026-04-07 12:47:38', '2026-04-07 12:47:38', 0);
INSERT INTO `schedule` VALUES (24, 5, '2025-12-28 00:00:00', '中班', '2025-12-28 13:06:52', '2025-12-28 17:06:52', 1, '', 2, '2026-04-07 13:07:10', '2026-04-07 13:07:10', 0);
INSERT INTO `schedule` VALUES (25, 5, '2025-12-29 00:00:00', '晚班', '2025-12-29 16:07:12', '2025-12-29 23:07:12', 1, '', 2, '2026-04-07 13:07:30', '2026-04-07 13:07:30', 0);
INSERT INTO `schedule` VALUES (26, 5, '2026-01-07 00:00:00', '早班', '2026-01-07 07:07:36', '2026-01-07 18:07:36', 1, '', 2, '2026-04-07 13:07:52', '2026-04-07 13:07:52', 0);
INSERT INTO `schedule` VALUES (27, 5, '2026-04-20 00:00:00', '中班', '2026-04-20 12:15:53', '2026-04-20 20:15:53', 1, '中午班', 2, '2026-04-24 22:16:59', '2026-04-24 22:16:59', 0);
INSERT INTO `schedule` VALUES (28, 5, '2026-04-21 00:00:00', '早班', '2026-04-21 08:00:00', '2026-04-21 16:00:00', 1, '仓库早班', 2, '2026-04-24 22:17:36', '2026-04-24 22:17:36', 0);
INSERT INTO `schedule` VALUES (29, 5, '2026-04-22 00:00:00', '晚班', '2026-04-22 18:00:23', '2026-04-22 23:00:23', 1, '炸货岛晚班', 2, '2026-04-24 22:18:57', '2026-04-24 22:18:57', 0);

-- ----------------------------
-- Table structure for supplier
-- ----------------------------
DROP TABLE IF EXISTS `supplier`;
CREATE TABLE `supplier`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '供应商ID',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '供应商名称',
  `contact` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '联系人',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '联系电话',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '邮箱',
  `address` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '地址',
  `bank_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '开户银行',
  `bank_account` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '银行账号',
  `status` int(0) DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
  `description` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '供应商描述',
  `create_time` datetime(0) DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime(0) DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `deleted` int(0) DEFAULT 0 COMMENT '是否删除：0-否，1-是',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '供应商表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of supplier
-- ----------------------------
INSERT INTO `supplier` VALUES (1, '华润万家供应链', '张总', '021-12345678', 'hr@crv.com', '上海市浦东新区张江高科技园区', '中国工商银行', '6222021234567890123', 1, '大型综合供应商', '2026-03-17 20:18:25', '2026-03-17 20:18:25', 0);
INSERT INTO `supplier` VALUES (2, '永辉生鲜配送', '李经理', '021-87654321', 'fresh@yonghui.com', '上海市闵行区莘庄工业区', '中国建设银行', '6227001234567890123', 1, '专业生鲜供应商', '2026-03-17 20:18:25', '2026-03-17 20:18:25', 0);
INSERT INTO `supplier` VALUES (3, '蒙牛乳业', '王经理', '010-11112222', 'business@mengniu.com', '内蒙古呼和浩特市和林格尔县', '中国农业银行', '6228481234567890123', 1, '乳制品供应商', '2026-03-17 20:18:25', '2026-03-17 20:18:25', 0);
INSERT INTO `supplier` VALUES (4, '金龙鱼食品', '赵经理', '021-33334444', 'sales@jinlongyu.com', '上海市浦东新区外高桥保税区', '中国银行', '6216601234567890123', 1, '粮油供应商', '2026-03-17 20:18:25', '2026-03-17 20:18:25', 0);
INSERT INTO `supplier` VALUES (5, '宝洁中国', '刘经理', '020-55556666', 'pg@pg.com', '广州市天河区珠江新城', '招商银行', '6214831234567890123', 1, '日用品供应商', '2026-03-17 20:18:25', '2026-03-17 20:18:25', 0);
INSERT INTO `supplier` VALUES (6, '联合利华', '陈经理', '021-77778888', 'ul@unilever.com', '上海市长宁区虹桥开发区', '交通银行', '6212601234567890123', 1, '日化用品供应商', '2026-03-17 20:18:25', '2026-03-17 20:18:25', 0);
INSERT INTO `supplier` VALUES (7, '农夫山泉', '孙经理', '0571-99990000', 'nfsq@nongfu.com', '浙江省杭州市西湖区', '中国工商银行', '6222021234567890456', 1, '饮料供应商', '2026-03-17 20:18:25', '2026-03-17 20:18:25', 0);
INSERT INTO `supplier` VALUES (8, '三只松鼠', '周经理', '0553-1234567', 'business@3songshu.com', '安徽省芜湖市弋江区', '中国建设银行', '6227001234567890456', 1, '零食供应商', '2026-03-17 20:18:25', '2026-03-17 20:18:25', 0);
INSERT INTO `supplier` VALUES (9, '海尔集团', '吴经理', '0532-88889999', 'haier@haier.com', '山东省青岛市崂山区', '中国农业银行', '6228481234567890456', 1, '家电供应商', '2026-03-17 20:18:25', '2026-03-17 20:18:25', 0);
INSERT INTO `supplier` VALUES (10, '蓝月亮', '郑经理', '020-11112233', 'bluemoon@bluemoon.com', '广州市黄埔区科学城', '中国银行', '6216601234567890456', 1, '清洁用品供应商', '2026-03-17 20:18:25', '2026-03-17 20:18:25', 0);
INSERT INTO `supplier` VALUES (11, '测试供应商', '张三', '18738422054', '', '', '', '', 1, '', '2026-03-20 17:07:16', '2026-03-22 18:17:03', 1);
INSERT INTO `supplier` VALUES (12, '测试', '133', '1873', '', '', '', '', 1, '', '2026-04-22 12:40:44', '2026-04-22 12:40:52', 1);

-- ----------------------------
-- Table structure for sys_department
-- ----------------------------
DROP TABLE IF EXISTS `sys_department`;
CREATE TABLE `sys_department`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '部门ID',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '部门名称',
  `description` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '部门描述',
  `parent_id` bigint(0) DEFAULT 0 COMMENT '父部门ID',
  `sort` int(0) DEFAULT 0 COMMENT '排序',
  `status` int(0) DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
  `create_time` datetime(0) DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime(0) DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `deleted` int(0) DEFAULT 0 COMMENT '是否删除：0-否，1-是',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '部门表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_department
-- ----------------------------
INSERT INTO `sys_department` VALUES (1, '总经办', '公司最高管理部门', 0, 1, 1, '2026-03-17 20:18:25', '2026-03-17 20:18:25', 0);
INSERT INTO `sys_department` VALUES (2, '采购部', '负责商品采购和供应商管理', 0, 2, 1, '2026-03-17 20:18:25', '2026-03-17 20:18:25', 0);
INSERT INTO `sys_department` VALUES (3, '仓储部', '负责库存管理和仓库运营', 0, 3, 1, '2026-03-17 20:18:25', '2026-03-17 20:18:25', 0);
INSERT INTO `sys_department` VALUES (4, '销售部', '负责商品销售和客户服务', 0, 4, 1, '2026-03-17 20:18:25', '2026-03-17 20:18:25', 0);
INSERT INTO `sys_department` VALUES (5, '财务部', '负责财务管理和资金运作', 0, 5, 1, '2026-03-17 20:18:25', '2026-03-17 20:18:25', 0);
INSERT INTO `sys_department` VALUES (6, '人力资源部', '负责人员招聘和培训管理', 0, 6, 1, '2026-03-17 20:18:25', '2026-03-17 20:18:25', 0);
INSERT INTO `sys_department` VALUES (7, '生鲜部', '负责生鲜商品管理', 3, 1, 1, '2026-03-17 20:18:25', '2026-03-17 20:18:25', 0);
INSERT INTO `sys_department` VALUES (8, '百货部', '负责百货商品管理', 3, 2, 1, '2026-03-17 20:18:25', '2026-03-17 20:18:25', 0);
INSERT INTO `sys_department` VALUES (9, '测试部门', '', 0, 6, 0, '2026-03-20 17:05:13', '2026-03-20 17:06:09', 0);
INSERT INTO `sys_department` VALUES (10, '测试', '', 0, 0, 1, '2026-03-22 18:30:42', '2026-03-22 18:31:28', 1);
INSERT INTO `sys_department` VALUES (11, '测试1', '', 1, 1, 1, '2026-03-22 18:30:58', '2026-03-22 18:31:25', 1);
INSERT INTO `sys_department` VALUES (12, '测试', '不选择上级部门就是顶级部门，排序的号是越小越靠上', 0, 0, 1, '2026-03-22 18:33:59', '2026-03-22 18:33:59', 0);

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码',
  `real_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '真实姓名',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '手机号',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '邮箱',
  `avatar` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '头像',
  `role` int(0) DEFAULT 3 COMMENT '角色：1-超级管理员，2-经理，3-员工',
  `status` int(0) DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
  `department_id` bigint(0) DEFAULT NULL COMMENT '部门ID',
  `position` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '职位',
  `create_time` datetime(0) DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime(0) DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `deleted` int(0) DEFAULT 0 COMMENT '是否删除：0-否，1-是',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`username`) USING BTREE,
  INDEX `idx_user_department`(`department_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, 'admin', 'e10adc3949ba59abbe56e057f20f883e', '系统管理员', '13800000001', 'admin@supermarket.com', NULL, 1, 1, 1, '系统管理员', '2026-03-17 20:18:25', '2026-03-17 20:18:25', 0);
INSERT INTO `sys_user` VALUES (2, 'manager1', 'e10adc3949ba59abbe56e057f20f883e', '张经理', '17530583207', 'zhangmanager@supermarket.com', 'https://supermarket-logistics-service.oss-cn-beijing.aliyuncs.com/avatar/93a04215-8d13-4db8-99a6-a7982591db67-20260324-蕾塞1.png', 2, 1, 1, '总经理', '2026-03-17 20:18:25', '2026-03-17 20:18:25', 0);
INSERT INTO `sys_user` VALUES (3, 'manager2', 'e10adc3949ba59abbe56e057f20f883e', '李经理', '13800000003', 'li@supermarket.com', NULL, 2, 1, 2, '采购经理', '2026-03-17 20:18:25', '2026-03-17 20:18:25', 0);
INSERT INTO `sys_user` VALUES (4, 'manager3', 'e10adc3949ba59abbe56e057f20f883e', '王经理', '13800000004', 'wang@supermarket.com', NULL, 2, 1, 5, '财务经理', '2026-03-17 20:18:25', '2026-03-17 20:18:25', 0);
INSERT INTO `sys_user` VALUES (5, 'employee1', 'e10adc3949ba59abbe56e057f20f883e', '张三', '18738422054', 'zhang@supermarket.com', 'https://supermarket-logistics-service.oss-cn-beijing.aliyuncs.com/avatar/a96c637d-7b24-4076-9b9c-25fa723bb39d-20260329-彩姐4.png', 3, 1, 2, '采购员', '2026-03-17 20:18:25', '2026-03-17 20:18:25', 0);
INSERT INTO `sys_user` VALUES (6, 'employee2', 'e10adc3949ba59abbe56e057f20f883e', '李四', '13800000006', 'qian@supermarket.com', NULL, 3, 1, 3, '仓管员', '2026-03-17 20:18:25', '2026-04-24 22:12:44', 0);
INSERT INTO `sys_user` VALUES (7, 'employee3', 'e10adc3949ba59abbe56e057f20f883e', '王五', '13800000007', 'sun@supermarket.com', NULL, 3, 1, 4, '销售员', '2026-03-17 20:18:25', '2026-04-24 22:12:44', 0);
INSERT INTO `sys_user` VALUES (8, 'employee4', 'e10adc3949ba59abbe56e057f20f883e', '周小花', '13800000008', 'zhou@supermarket.com', NULL, 3, 1, 5, '会计', '2026-03-17 20:18:25', '2026-03-17 20:18:25', 0);
INSERT INTO `sys_user` VALUES (9, 'employee5', 'e10adc3949ba59abbe56e057f20f883e', '吴小强', '13800000009', 'wu@supermarket.com', NULL, 3, 1, 6, '人事专员', '2026-03-17 20:18:25', '2026-03-17 20:18:25', 0);
INSERT INTO `sys_user` VALUES (10, 'employee6', 'e10adc3949ba59abbe56e057f20f883e', '郑小龙', '13800000010', 'zheng@supermarket.com', NULL, 3, 1, 3, '生鲜仓管', '2026-03-17 20:18:25', '2026-03-17 20:18:25', 0);
INSERT INTO `sys_user` VALUES (11, 'employee7', 'e10adc3949ba59abbe56e057f20f883e', '王小芳', '13800000011', 'wang2@supermarket.com', NULL, 3, 1, 3, '百货仓管', '2026-03-17 20:18:25', '2026-03-17 20:18:25', 0);
INSERT INTO `sys_user` VALUES (12, 'employee8', 'e10adc3949ba59abbe56e057f20f883e', '陈小伟', '13800000012', 'chen@supermarket.com', NULL, 3, 1, 2, '采购助理', '2026-03-17 20:18:25', '2026-03-17 20:18:25', 0);
INSERT INTO `sys_user` VALUES (13, 'nameura', 'e10adc3949ba59abbe56e057f20f883e', '光头强', '17530583207', '1290127634@qq.com', NULL, 3, 1, NULL, NULL, '2026-03-22 17:22:12', '2026-03-22 17:22:12', 0);
INSERT INTO `sys_user` VALUES (14, 'newuser', '25f9e794323b453885f5181f1b624d0b', '桑吉尔夫', '15937994075', '1290127634@qq.com', NULL, 3, 1, NULL, NULL, '2026-04-24 22:14:55', '2026-04-24 22:14:55', 0);

SET FOREIGN_KEY_CHECKS = 1;
