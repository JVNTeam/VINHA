USE ViNha;
GO

-- 1) Sửa các bản ghi lỗi cụ thể đang gặp
UPDATE mon_an
SET ten = N'Cơm bò lúc lắc'
WHERE ten = N'Com bò lúc l?c' OR ten = N'Cơm bò lúc l?c';

UPDATE mon_an
SET mo_ta = N'Bò mềm'
WHERE mo_ta = N'Bò m?m';

-- 2) Có thể mở rộng sửa lỗi phổ biến dấu '?'
--    Chạy SELECT trước để kiểm tra các dòng lỗi
SELECT id, ten, mo_ta
FROM mon_an
WHERE ten LIKE N'%?%'
   OR mo_ta LIKE N'%?%';

-- 3) Ví dụ mẫu sửa thêm (bạn thay đúng giá trị cần sửa)
-- UPDATE mon_an SET ten = N'Cơm gà chiên' WHERE id = 1;
-- UPDATE mon_an SET mo_ta = N'Cơm gà giòn' WHERE id = 1;

GO
