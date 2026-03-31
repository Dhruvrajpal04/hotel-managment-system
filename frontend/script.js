const API_URL = 'http://localhost:3000/api';

document.addEventListener('DOMContentLoaded', () => {
    // Auth Check
    const userRole = localStorage.getItem('userRole');
    const username = localStorage.getItem('username');
    if (!userRole || !username) {
        window.location.href = 'login.html';
        return;
    }

    // Dynamic UI Injection
    const capitalizedName = username.charAt(0).toUpperCase() + username.slice(1);
    const capitalizedRole = userRole.charAt(0).toUpperCase() + userRole.slice(1);
    document.getElementById('welcome-subtitle').textContent = `Welcome back, ${capitalizedName}`;
    document.getElementById('user-profile-name').textContent = capitalizedName;
    document.getElementById('user-profile-role').textContent = capitalizedRole;
    document.getElementById('user-avatar').src = `https://api.dicebear.com/7.x/avataaars/svg?seed=${capitalizedName}`;

    // Navigation
    const navLinks = document.querySelectorAll('.nav-links li[data-page]');
    const pageTitle = document.getElementById('page-title');

    navLinks.forEach(link => {
        link.addEventListener('click', () => {
            navLinks.forEach(l => l.classList.remove('active'));
            link.classList.add('active');
            
            const page = link.getAttribute('data-page');
            if (page && page !== 'settings') { // ignoring settings for now
                pageTitle.textContent = page.charAt(0).toUpperCase() + page.slice(1);
                switchTab(page);
            }
        });
    });

    const logoutBtn = document.getElementById('logout-btn');
    if (logoutBtn) {
        logoutBtn.addEventListener('click', () => {
            localStorage.removeItem('userRole');
            localStorage.removeItem('username');
            window.location.href = 'login.html';
        });
    }
    // Modals Open
    document.querySelectorAll('.add-booking-btn').forEach(btn => {
        btn.addEventListener('click', () => {
            document.getElementById('booking-modal').style.display = 'block';
        });
    });

    document.getElementById('add-customer-btn').addEventListener('click', () => {
        document.getElementById('customer-modal').style.display = 'block';
    });

    // Modals Close
    document.querySelectorAll('.close, .cancel-modal').forEach(btn => {
        btn.addEventListener('click', (e) => {
            const modalId = e.target.getAttribute('data-modal');
            if (modalId) {
                document.getElementById(modalId).style.display = 'none';
            }
        });
    });

    window.addEventListener('click', (e) => {
        if (e.target.classList.contains('modal')) {
            e.target.style.display = 'none';
        }
    });

    // Forms
    document.getElementById('booking-form').addEventListener('submit', async (e) => {
        e.preventDefault();
        
        const name = document.getElementById('book-cust-name').value;
        const phone = document.getElementById('book-cust-phone').value;
        const email = document.getElementById('book-cust-email').value;
        const adhaar = document.getElementById('book-cust-adhaar').value;
        const rid = document.getElementById('room-id').value;

        try {
            // 1. Create User
            const custResponse = await fetch(`${API_URL}/customers`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ name, phone, email, adhaar })
            });

            if (!custResponse.ok) {
                const err = await custResponse.json();
                alert('Failed to add customer. Error: ' + JSON.stringify(err));
                return;
            }

            const custData = await custResponse.json();
            const cid = custData.customer_id;

            // 2. Book Room
            const bookResponse = await fetch(`${API_URL}/bookings`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ customer_id: cid, room_id: rid })
            });

            if (bookResponse.ok) {
                alert('Customer Added and Room Booked!');
                document.getElementById('booking-modal').style.display = 'none';
                document.getElementById('booking-form').reset();
                refreshCurrentView();
            } else {
                const err = await bookResponse.json();
                alert('Failed to create booking. Error: ' + JSON.stringify(err));
            }
        } catch (error) {
            console.error('Error during new booking flow:', error);
            alert('An error occurred. Make sure the server is running.');
        }
    });

    document.getElementById('customer-form').addEventListener('submit', async (e) => {
        e.preventDefault();
        const name = document.getElementById('cust-name').value;
        const phone = document.getElementById('cust-phone').value;
        const email = document.getElementById('cust-email').value;
        const adhaar = document.getElementById('cust-adhaar').value;
        
        try {
            const response = await fetch(`${API_URL}/customers`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ name, phone, email, adhaar })
            });

            if (response.ok) {
                alert('Customer Added!');
                document.getElementById('customer-modal').style.display = 'none';
                document.getElementById('customer-form').reset();
                loadCustomers();
            } else {
                const err = await response.json();
                alert('Failed to add customer. Error: ' + JSON.stringify(err));
            }
        } catch (error) {
            console.error('Error adding customer:', error);
        }
    });

    // Global Search
    const searchInput = document.getElementById('global-search');
    if (searchInput) {
        searchInput.addEventListener('input', (e) => {
            const searchTerm = e.target.value.toLowerCase();
            const activeView = document.querySelector('.page-view:not(.hidden)');
            if (!activeView) return;
            
            const rows = activeView.querySelectorAll('tbody tr');
            rows.forEach(row => {
                // Ignore "loading/empty" rows (usually single big column)
                if (row.cells && row.cells.length === 1) return;
                
                const textContent = row.textContent.toLowerCase();
                row.style.display = textContent.includes(searchTerm) ? '' : 'none';
            });
        });
    }

    // Initial Load
    switchTab('dashboard');
});

function switchTab(pageId) {
    const searchInput = document.getElementById('global-search');
    if (searchInput) searchInput.value = ''; // Reset search on tab change

    document.querySelectorAll('.page-view').forEach(view => view.classList.add('hidden'));
    const targetView = document.getElementById(`${pageId}-view`);
    if(targetView) {
        targetView.classList.remove('hidden');
    }
    
    if(pageId === 'dashboard') {
        loadDashboard();
    } else if(pageId === 'rooms') {
        loadRooms();
    } else if(pageId === 'bookings') {
        loadAllBookings();
    } else if(pageId === 'customers') {
        loadCustomers();
    }
}

function refreshCurrentView() {
    const activeNav = document.querySelector('.nav-links li.active');
    if (activeNav) {
        switchTab(activeNav.getAttribute('data-page'));
    }
}

async function loadDashboard() {
    try {
        const [roomsResp, bookingsResp] = await Promise.all([
            fetch(`${API_URL}/rooms`),
            fetch(`${API_URL}/bookings`)
        ]);

        const rooms = await roomsResp.json();
        const bookings = await bookingsResp.json();

        // Update Stats
        document.getElementById('total-rooms-count').textContent = rooms.length;
        document.getElementById('booked-rooms-count').textContent = rooms.filter(r => r.status === 'Booked').length;
        document.getElementById('available-rooms-count').textContent = rooms.filter(r => r.status === 'Available').length;
        
        // Calculate Total Income
        let totalIncome = 0;
        bookings.forEach(b => {
            if (b.check_out) {
                const checkInDate = new Date(b.check_in);
                const checkOutDate = new Date(b.check_out);
                const diffTime = Math.abs(checkOutDate.getTime() - checkInDate.getTime());
                const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
                
                const room = rooms.find(r => r.room_id === b.room_id);
                if (room) {
                    totalIncome += (diffDays * room.price);
                }
            }
        });
        document.getElementById('total-income').textContent = '$' + totalIncome;
        
        // Populate recent bookings (last 5)
        const recentBookings = [...bookings].reverse().slice(0, 5);
        const tbody = document.getElementById('bookings-table-body');
        
        if (recentBookings.length === 0) {
            tbody.innerHTML = '<tr><td colspan="7" style="text-align: center; color: #8b949e;">No bookings found.</td></tr>';
        } else {
            tbody.innerHTML = recentBookings.map(b => `
                <tr>
                    <td>#${b.booking_id}</td>
                    <td>${b.name}</td>
                    <td>Room ${b.room_id}</td>
                    <td>${new Date(b.check_in).toLocaleDateString()}</td>
                    <td>${b.check_out ? new Date(b.check_out).toLocaleDateString() : '-'}</td>
                    <td><span class="status ${b.check_out ? 'confirmed' : 'checking-out'}">${b.check_out ? 'Completed' : 'Active'}</span></td>
                    <td>
                        ${!b.check_out ? `<button class="btn btn-secondary" style="padding: 5px 10px; font-size: 0.8rem;" onclick="openCheckoutModal(${b.room_id}, '${(b.name || '').replace(/'/g, "\\'")}', '${(b.phone || '').replace(/'/g, "\\'")}')">Checkout</button>` : 'Done'}
                    </td>
                </tr>
            `).join('');
        }
    } catch (error) {
        console.error('Error loading dashboard:', error);
    }
}

async function loadRooms() {
    try {
        const res = await fetch(`${API_URL}/rooms`);
        const rooms = await res.json();
        const tbody = document.getElementById('rooms-table-body');
        
        if (rooms.length === 0) {
            tbody.innerHTML = '<tr><td colspan="4" style="text-align: center; color: #8b949e;">No rooms found.</td></tr>';
            return;
        }

        tbody.innerHTML = rooms.map(r => `
            <tr>
                <td>${r.room_id}</td>
                <td>${r.room_type}</td>
                <td>$${r.price}</td>
                <td><span class="status ${r.status === 'Available' ? 'confirmed' : 'checking-out'}">${r.status}</span></td>
            </tr>
        `).join('');
    } catch (error) {
        console.error('Error loading rooms:', error);
    }
}

async function loadAllBookings() {
    try {
        const res = await fetch(`${API_URL}/bookings`);
        const bookings = await res.json();
        const tbody = document.getElementById('all-bookings-table-body');
        
        if (bookings.length === 0) {
            tbody.innerHTML = '<tr><td colspan="9" style="text-align: center; color: #8b949e;">No bookings found.</td></tr>';
            return;
        }

        tbody.innerHTML = bookings.map(b => `
            <tr>
                <td>${b.booking_id}</td>
                <td>${b.customer_id}</td>
                <td>${b.name}</td>
                <td>${b.phone}</td>
                <td>${b.adhaar}</td>
                <td>${b.room_id}</td>
                <td>${new Date(b.check_in).toLocaleDateString()}</td>
                <td>${b.check_out ? new Date(b.check_out).toLocaleDateString() : '-'}</td>
                <td>
                    ${!b.check_out ? `<button class="btn btn-secondary" style="padding: 5px 10px; font-size: 0.8rem;" onclick="openCheckoutModal(${b.room_id}, '${(b.name || '').replace(/'/g, "\\'")}', '${(b.phone || '').replace(/'/g, "\\'")}')">Checkout</button>` : 'Done'}
                </td>
            </tr>
        `).join('');
    } catch (error) {
        console.error('Error loading all bookings:', error);
    }
}

async function loadCustomers() {
    try {
        const res = await fetch(`${API_URL}/customers`);
        const customers = await res.json();
        const tbody = document.getElementById('customers-table-body');
        
        if (customers.length === 0) {
            tbody.innerHTML = '<tr><td colspan="5" style="text-align: center; color: #8b949e;">No customers found.</td></tr>';
            return;
        }

        tbody.innerHTML = customers.map(c => `
            <tr>
                <td>${c.customer_id}</td>
                <td>${c.name}</td>
                <td>${c.phone}</td>
                <td>${c.email}</td>
                <td>${c.adhaar}</td>
            </tr>
        `).join('');
    } catch (error) {
        console.error('Error loading customers:', error);
    }
}

window.openCheckoutModal = function(roomId, custName, custPhone) {
    const days = prompt("Enter days stayed:");
    if (!days || isNaN(days)) return;
    
    fetch(`${API_URL}/checkout`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ room_id: roomId, days: parseInt(days) })
    })
    .then(async res => {
        if (!res.ok) {
            const err = await res.json();
            throw new Error(err.message || JSON.stringify(err));
        }
        return res.json();
    })
    .then(data => {
        if(data.message === 'Checkout Done') {
            let whatsappHtml = "";
            if (custName && custPhone) {
                const waMessage = `Hello ${custName},\n\nYour checkout for Room ${roomId} at Royal Stay is complete.\nDays Stayed: ${days}\n*Total Bill: $${data.total_bill}*\n\nThank you for staying with us!`;
                let cleanedPhone = custPhone.replace(/\D/g, '');
                // basic validation: if it's 10 digits add +91 (India) as default, else just use the digits
                if(cleanedPhone.length === 10) cleanedPhone = '91' + cleanedPhone;
                
                const waUrl = `https://wa.me/${cleanedPhone}?text=${encodeURIComponent(waMessage)}`;
                whatsappHtml = `
                    <a href="${waUrl}" target="_blank" class="btn" style="background-color: #25D366; color: white; border: none; padding: 10px 15px; border-radius: 6px; text-decoration: none; display: inline-flex; align-items: center; gap: 8px;">
                        <i class="fa-brands fa-whatsapp" style="font-size: 1.2rem;"></i> Send Bill
                    </a>
                `;
            }

            document.getElementById('checkout-details').innerHTML = `
                <div id="invoice-content" style="padding: 10px; background-color: #f8f9fa; color: #333; border-radius: 8px; margin-bottom: 20px;">
                    <h3 style="margin-top: 0; color: #111;">Royal Stay Invoice</h3>
                    <p><strong>Customer:</strong> ${custName || 'Walk-in'}</p>
                    <p><strong>Room ID:</strong> ${roomId}</p>
                    <p><strong>Days Stayed:</strong> ${days}</p>
                    <hr style="border-color: #ccc; margin: 15px 0;">
                    <p>Total Bill: <strong style="color: #28a745; font-size: 1.5rem;">$${data.total_bill}</strong></p>
                </div>
                ${whatsappHtml ? `<div style="margin-top: 15px;">${whatsappHtml}</div>` : ''}
            `;
            
            document.getElementById('checkout-modal').style.display = 'block';
            refreshCurrentView();
        } else {
            alert('Error: ' + JSON.stringify(data));
        }
    })
    .catch(err => {
        console.error(err);
        alert('Checkout Failed: ' + err.message);
    });
};
