import { motion, AnimatePresence } from 'framer-motion';
import { Trophy, Award, Medal, Zap, Star } from 'lucide-react';

const RewardAnimation = ({ show, rewardType, description, onClose }) => {
    if (!show) return null;

    const getRewardIcon = (type) => {
        switch (type) {
            case 'gold_medal':
                return <Trophy size={80} style={{ color: '#FFD700' }} />;
            case 'silver_medal':
                return <Medal size={80} style={{ color: '#C0C0C0' }} />;
            case 'bronze_medal':
                return <Medal size={80} style={{ color: '#CD7F32' }} />;
            case 'speed_master':
                return <Zap size={80} style={{ color: '#3B82F6' }} />;
            case 'perfect_run':
                return <Star size={80} style={{ color: '#FBBF24' }} />;
            default:
                return <Award size={80} style={{ color: '#10B981' }} />;
        }
    };

    const getRewardColor = (type) => {
        switch (type) {
            case 'gold_medal':
                return '#FFD700';
            case 'silver_medal':
                return '#C0C0C0';
            case 'bronze_medal':
                return '#CD7F32';
            case 'speed_master':
                return '#3B82F6';
            case 'perfect_run':
                return '#FBBF24';
            default:
                return '#10B981';
        }
    };

    return (
        <AnimatePresence>
            <motion.div
                initial={{ opacity: 0 }}
                animate={{ opacity: 1 }}
                exit={{ opacity: 0 }}
                style={{
                    position: 'fixed',
                    inset: 0,
                    background: 'rgba(0, 0, 0, 0.8)',
                    display: 'flex',
                    alignItems: 'center',
                    justifyContent: 'center',
                    zIndex: 1000,
                    backdropFilter: 'blur(5px)'
                }}
                onClick={onClose}
            >
                <motion.div
                    initial={{ scale: 0, rotate: -180 }}
                    animate={{ scale: 1, rotate: 0 }}
                    exit={{ scale: 0, rotate: 180 }}
                    transition={{ type: 'spring', duration: 0.8 }}
                    style={{
                        background: 'linear-gradient(135deg, rgba(30, 30, 30, 0.95), rgba(50, 50, 50, 0.95))',
                        padding: '50px',
                        borderRadius: '20px',
                        textAlign: 'center',
                        maxWidth: '500px',
                        border: `3px solid ${getRewardColor(rewardType)}`,
                        boxShadow: `0 0 50px ${getRewardColor(rewardType)}80`
                    }}
                    onClick={(e) => e.stopPropagation()}
                >
                    {/* Icono animado */}
                    <motion.div
                        animate={{
                            scale: [1, 1.2, 1],
                            rotate: [0, 10, -10, 0]
                        }}
                        transition={{
                            duration: 2,
                            repeat: Infinity,
                            ease: 'easeInOut'
                        }}
                        style={{ marginBottom: '30px' }}
                    >
                        {getRewardIcon(rewardType)}
                    </motion.div>

                    {/* Título */}
                    <motion.h2
                        initial={{ y: 20, opacity: 0 }}
                        animate={{ y: 0, opacity: 1 }}
                        transition={{ delay: 0.3 }}
                        style={{
                            fontSize: '2.5rem',
                            color: getRewardColor(rewardType),
                            marginBottom: '20px',
                            fontWeight: 'bold',
                            textShadow: `0 0 20px ${getRewardColor(rewardType)}80`
                        }}
                    >
                        ¡Recompensa Obtenida!
                    </motion.h2>

                    {/* Descripción */}
                    <motion.p
                        initial={{ y: 20, opacity: 0 }}
                        animate={{ y: 0, opacity: 1 }}
                        transition={{ delay: 0.5 }}
                        style={{
                            fontSize: '1.3rem',
                            color: '#fff',
                            marginBottom: '30px'
                        }}
                    >
                        {description}
                    </motion.p>

                    {/* Botón */}
                    <motion.button
                        initial={{ y: 20, opacity: 0 }}
                        animate={{ y: 0, opacity: 1 }}
                        transition={{ delay: 0.7 }}
                        whileHover={{ scale: 1.05 }}
                        whileTap={{ scale: 0.95 }}
                        onClick={onClose}
                        className="btn-primary"
                        style={{
                            padding: '15px 40px',
                            fontSize: '1.2rem',
                            background: `linear-gradient(135deg, ${getRewardColor(rewardType)}, ${getRewardColor(rewardType)}80)`,
                            border: 'none',
                            cursor: 'pointer'
                        }}
                    >
                        ¡Genial!
                    </motion.button>

                    {/* Partículas de fondo */}
                    {[...Array(20)].map((_, i) => (
                        <motion.div
                            key={i}
                            initial={{
                                x: 0,
                                y: 0,
                                opacity: 1,
                                scale: Math.random() * 0.5 + 0.5
                            }}
                            animate={{
                                x: (Math.random() - 0.5) * 400,
                                y: (Math.random() - 0.5) * 400,
                                opacity: 0,
                                scale: 0
                            }}
                            transition={{
                                duration: 2,
                                delay: i * 0.05,
                                repeat: Infinity,
                                repeatDelay: 1
                            }}
                            style={{
                                position: 'absolute',
                                width: '10px',
                                height: '10px',
                                borderRadius: '50%',
                                background: getRewardColor(rewardType),
                                top: '50%',
                                left: '50%',
                                pointerEvents: 'none'
                            }}
                        />
                    ))}
                </motion.div>
            </motion.div>
        </AnimatePresence>
    );
};

export default RewardAnimation;
