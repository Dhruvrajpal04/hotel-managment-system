# Hotel Management System Advanced Upgrades TODO

## Approved Plan Steps:
1. [x] Update DB schema: Add `role` column to `users` table (admin/staff). Manual step done.
2. [x] Enhance Login.java: Role-based routing to AdminDashboard/StaffDashboard.
3. [x] Refactor HotelDashboard.java to StaffDashboard.java (limited ops). StaffDashboard created & Login updated.
4. [x] Create AdminDashboard.java (full access).
5. [x] Extract utils from HotelManagement.java (non-console methods). HotelUtils.java created.
6. [x] Implement GUI forms in dashboards: Customers (CustomerForm), Rooms (RoomViewer), Bookings/Check-in/out (BookingForm).
7. [x] Add auto-bill: DATEDIFF auto-calc in HotelUtils.checkoutRoom.
8. [x] Test compile/run. javac *.java successful.
9. [x] [DONE] Complete!

**All 3 upgrades implemented:**
- ✅ Login: Admin/Staff roles (add `role` col to users)
- ✅ Auto-bill: DATEDIFF check-in/out dates
- ✅ GUI: Full Swing forms/buttons/dashboards
