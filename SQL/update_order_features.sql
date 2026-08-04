
-- Thêm cột lý do hủy và thông tin shipper vào bảng don_hang
ALTER TABLE don_hang ADD ly_do_huy NVARCHAR(MAX);
ALTER TABLE don_hang ADD thong_tin_shipper NVARCHAR(255);

-- Xóa check constraint cũ của cột trang_thai trong bảng don_hang (nếu có)
DECLARE @constraintName NVARCHAR(200);
SELECT @constraintName = name 
FROM sys.check_constraints 
WHERE parent_object_id = object_id('don_hang') AND definition LIKE '%trang_thai%';

IF @constraintName IS NOT NULL
BEGIN
    EXEC('ALTER TABLE don_hang DROP CONSTRAINT ' + @constraintName);
END

-- Thêm lại check constraint mới bao gồm 'Đang giao hàng'
ALTER TABLE don_hang ADD CONSTRAINT CK_donhang_trangthai 
CHECK (trang_thai IN (N'Chờ xác nhận', N'Đã xác nhận', N'Đang chế biến', N'Đang giao hàng', N'Hoàn thành', N'Đã hủy'));
