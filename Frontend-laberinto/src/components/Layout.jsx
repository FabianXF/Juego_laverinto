import Navbar from './Navbar';

const Layout = ({ children }) => {
    return (
        <div style={{ minHeight: '100vh', display: 'flex', flexDirection: 'column' }}>
            <Navbar />
            <main className="container" style={{ flex: 1 }}>
                {children}
            </main>
        </div>
    );
};

export default Layout;
